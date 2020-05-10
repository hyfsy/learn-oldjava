package designmodel.other.memento;

/**
 * 备忘录模式
 * 
 * 记录一个对象的内部状态，以便在以后的任意时刻，在不破坏封装的情况下回退
 * 容易产生大量对象，增加系统开销
 * 
 * @author baB_hyf
 */
public class Test
{

    public static void main(String[] args) {
        // 创建备忘录管理者
        MementoManager manager = new MementoManager();

        Knight knight = new Knight(100);
        // 保存生成备忘录对象
        Memento saveMemento = knight.saveMemento();
        // 将备忘录保存
        manager.setMemento(saveMemento);
        System.out.println("保存存档：" + knight.getBlood());

        // 扣除骑士的血
        knight.setBlood(knight.getBlood() - 20);
        System.out.println("被怪物击中：" + knight.getBlood());

        // 获取最近的保存状态
        Memento memento = manager.getMemento();
        // 还原满血状态
        knight.restoreMemento(memento);
        System.out.println("还原存档：" + knight.getBlood());

    }

}
