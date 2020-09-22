package designpatterns.constructor;

import designpatterns.constructor.AbstractHouse;


//这里产品（房子）本身和建造过程（build）耦合了，可以用建造者模式，形成缓存层优化
public class CommonHouse extends AbstractHouse{
	@Override
	public void buildBasic(){
		System.out.println("普通房子打地基");
	}
	
	@Override
	public void buildWalls(){
		System.out.println("普通房子建墙");
		
	}
	
	@Override
	public void roofed(){
		System.out.println("普通房子盖屋顶");
	}
}