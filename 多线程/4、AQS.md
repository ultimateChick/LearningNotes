# AQS（CLH队列）

## 从源码层面理解ReentrantLock

### 源码阅读技巧

- 跑起来的代码才读！
- 解决问题就好 —— 带着目的去读
- 一条线索到底
- 无关细节略过
- 一般不读静态
- 一般动态读法

## AQS框架一例

![image-20200806130927876](C:\Users\q1367\Desktop\jdk\image-20200806130927876.png)

NonfairSync Sync AQS的关系

![image-20200806131648144](C:\Users\q1367\Desktop\jdk\image-20200806131648144.png)



## 核心属性：state

**state**： volatile int state

ReentractLock在调用lock方法，如果获得这把锁，state就会从0变成1，在可重入场景，state可以大于1

释放了就是从0变1

### 核心理解：

**state根据子类的不同实现，具有不同的意义！**

比如在使用CountDownLatch类，从构造器传入的int，就是使得state计数为5，每次countDown就减1，state为0时，解除await状态。

## 核心队列

AQS底层还维护了一个双向链表的实现：维护着head、tail和具体的Node定义

我们关注它的**Node**，它持有一个**Thread**引用

因此这是一个管理Thread的队列，即等待队列

多个Thread在进入队列时，也是CAS进入的

![image-20200728175659446](C:\Users\q1367\Desktop\jdk\图解AQS.png)

## 具体的acquire

ReentrantLock对象进到方法，先检查state是否为0

为0的情况下，调用compareAndSetState（expect：0，acquires）；（期望值是0，改成acquires

即CAS的方式，来改变state状态，实现锁的效果

而且，新的Node在加入等待队列也是CAS方式的

```java
@ReservedStackAccess
final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    int c = getState();
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            //设置持有此“锁”的线程
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    //如果当前线程再次lock，这里用以支持可重入性
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
    }
    return false;
}
```

## 详细过程

### **ReentrantLock.java**

![image-20200806152254903](C:\Users\q1367\AppData\Roaming\Typora\typora-user-images\image-20200806152254903.png)

```java
public void lock() {
    sync.acquire(1); //调用Sync类中的acquire方法
}
```

### **Sync:sync.acquire()**

它是ReentrantLock的一个抽象内部类，继承自AQS

```java
abstract static class Sync extends AbstractQueuedSynchronizer {
```

类中对非公平的tryAcquire方法做了实现



此抽象类有公平和非公平两种子类实现，也都定义在ReentrantLock中

它们的特殊之处在于对AQS框架中的tryAcquire方法做了不同的实现（NonFair的调用了Sync中实现好的方法



#### **上面的sync.acquire(1);**

是到了AQS框架中默认实现好的acquire方法



### **AbstractQueuedSynchronizer:acquire**

```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```

利用短路与，用tryAcquire尝试获取“锁”，获取不到就执行后续的acquireQueued方法

acquireQueued会把当前请求独占的线程纳入队列中，并尝试进行获取所有权

我们继续看**tryAcquire(arg)**



### FairSync:tryAcquire()

```java
    @ReservedStackAccess
    protected final boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (!hasQueuedPredecessors() &&
                compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
}
```



公平和非公平的tryAcquire方法实现逻辑很像，就是公平的方法在state==0的时候，还要进行一个hasQueuedPredecessors()方法的执行，这个方法会检查队列中是否有比当前线程等待更久的线程，如有就要放弃此次的占领。

但是在队列是空的情况下也可能会有多个线程通过此方法进行后续执行，barging现象还是不能避免的。

#### 情况1：通过CAS方式改动state成功

```java
setExclusiveOwnerThread(current);
```

此方法使得当前线程独占执行权

#### 情况2：CAS失败

那么就进入检查线程是否可重入，否则就返回false给acquire方法

此时，我们来到

```java
acquireQueued(addWaiter(Node.EXCLUSIVE), arg)
```



### AbstractQueuedSynchronizer:addWaiter

```java
acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
```

```java
/**
 * Creates and enqueues node for current thread and given mode.
 *
 * @param mode Node.EXCLUSIVE for exclusive, Node.SHARED for shared
 * @return the new node
 */
private Node addWaiter(Node mode) {
    Node node = new Node(mode);

    for (;;) {
        Node oldTail = tail;
        if (oldTail != null) {
            node.setPrevRelaxed(oldTail);
            if (compareAndSetTail(oldTail, node)) {
                oldTail.next = node;
                return node;
            }
        } else {
            initializeSyncQueue();
        }
    }
}
```

此方法会尝试在等待队列中插入代表当前线程的Node，操作是CAS的

如果没有尾巴节点，就新建同步等待队列

****

~~我们看到AQS用Node(Node node)的构造方法创建新的node~~

```java
/** Constructor used by addWaiter. */
Node(Node nextWaiter) {
    this.nextWaiter = nextWaiter;
    THREAD.set(this, Thread.currentThread());
}
```

~~我们探究Node中nextWaiter到底是个什么东西~~

```java
/**
 * Link to next node waiting on condition, or the special
 * value SHARED.  Because condition queues are accessed only
 * when holding in exclusive mode, we just need a simple
 * linked queue to hold nodes while they are waiting on
 * conditions. They are then transferred to the queue to
 * re-acquire. And because conditions can only be exclusive,
 * we save a field by using special value to indicate shared
 * mode.
 */
Node nextWaiter;
```

~~waiter是暂存要进入等待队列的Node~~

****

### AbstractQueuedSynchronizer:acquireQueued

```java
/**
 * Acquires in exclusive uninterruptible mode for thread already in
 * queue. Used by condition wait methods as well as acquire.
 *
 * @param node the node
 * @param arg the acquire argument
 * @return {@code true} if interrupted while waiting
 */
final boolean acquireQueued(final Node node, int arg) {
    boolean interrupted = false;
    try {
        for (;;) {
            final Node p = node.predecessor();
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                return interrupted;
            }
            if (shouldParkAfterFailedAcquire(p, node))
                interrupted |= parkAndCheckInterrupt();
        }
    } catch (Throwable t) {
        cancelAcquire(node);
        if (interrupted)
            selfInterrupt();
        throw t;
    }
}
```

通过获取当前线程的前一个Node节点，如果这个节点是头节点（表示可能正在持有执行权），就尝试当前线程获取执行权，如果成功，设置当前线程为头节点，并取消先前运行的节点的next对当前节点的引用，以防止内存泄漏。for(;;)代表的是自旋过程



### AbstractQueuedSynchronizer:Node:setPrevRelaxed(Node p)

```java
final void setPrevRelaxed(Node p) {
    PREV.set(this, p);
}
```

这里有一个很有意思的PREV变量，通过它在Node类中的定义可知它是VarHandle类型的，即变量句柄

在jdk1.9开始引入

#### VarHandle

一个简单的

Object o = new Object()

我们知道这创建了一个引用变量类型的o，指向了我们在对空间中划分出来的Object实例对象

VarHandle就代表着这个o，也可以创建指向同样对象的引用

特殊之处：

​	通过句柄操作实例属性时，支持CAS形式，这使得我们可以对先前的普通对象提供原子的特性。

![image-20200806170514054](C:\Users\q1367\Desktop\jdk\image-20200806170514054.png)

在没有VarHandle的以前，只能通过反射方式来操作，效率相比要低得多（需要检查）

而VarHandle底层是可以直接操作二进制码的

提高JUC的性能

