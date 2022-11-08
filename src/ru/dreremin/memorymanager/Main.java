package ru.dreremin.memorymanager;

import ru.dreremin.memorymanager.memory.MemoryManager;

public class Main {

    public static void main(String[] args) {

        System.out.println("MemoryManager");

        MemoryManager manager = new MemoryManager(100);
        System.out.println("start - " + manager);
        manager.malloc(101);
        System.out.println("malloc 101 - " + manager);
        manager.malloc(50);
        System.out.println("malloc 50 - " + manager);
        manager.malloc(20);
        System.out.println("malloc 20 - " + manager);
        manager.malloc(20);
        System.out.println("malloc 20 - " + manager);
        manager.free(0);
        System.out.println("free 0 - " + manager);
        manager.malloc(9);
        System.out.println("malloc 9 - " + manager);
        manager.free(50);
        System.out.println("free 50 - " + manager);
        manager.malloc(40);
        System.out.println("malloc 40 - " + manager);
        manager.free(25);
        System.out.println("free 25 - " + manager);
        manager.free(50);
        System.out.println("free 50 - " + manager);

        System.out.println("MemoryManager2");

        MemoryManager manager2 = new MemoryManager(100);
        System.out.println("start - " + manager2);
        manager2.malloc(101);
        System.out.println("malloc 101 - " + manager2);
        manager2.malloc(50);
        System.out.println("malloc 50 - " + manager2);
        manager2.malloc(20);
        System.out.println("malloc 20 - " + manager2);
        manager2.malloc(20);
        System.out.println("malloc 20 - " + manager2);
        manager2.free(0);
        System.out.println("free 0 - " + manager2);
        manager2.malloc(9);
        System.out.println("malloc 9 - " + manager2);
        manager2.free(50);
        System.out.println("free 50 - " + manager2);
        manager2.malloc(40);
        System.out.println("malloc 40 - " + manager2);
        manager2.free(25);
        System.out.println("free 25 - " + manager2);
        manager2.free(50);
        System.out.println("free 50 - " + manager2);
    }
}
