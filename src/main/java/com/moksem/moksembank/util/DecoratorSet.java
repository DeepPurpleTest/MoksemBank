package com.moksem.moksembank.util;

import com.moksem.moksembank.model.dto.Dto;
import lombok.EqualsAndHashCode;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
public class DecoratorSet extends AbstractSet<Dto.Param> {
    private final Set<Dto.Param> set;

    public DecoratorSet(Set<Dto.Param> set) {
        this.set = set;
    }

    @Override
    public Iterator<Dto.Param> iterator() {
        return set.iterator();
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean add(Dto.Param o) {
        if (!set.add(o))
            set.remove(o);

        return set.add(o);
    }

//    public static void main(String[] args) {
//        Set<Dto.Param> mySet = new HashSet<>();
//        DecoratorSet decoratorSet = new DecoratorSet(mySet);
//        decoratorSet.add(new Dto.Param("size", "10"));
//        System.out.println(mySet);
//        System.out.println(decoratorSet);
//        decoratorSet.add(new Dto.Param("size", "9"));
//        System.out.println(mySet);
//        System.out.println(decoratorSet);
//        mySet.add(new Dto.Param("size", "11"));
//        System.out.println(mySet);
//        System.out.println(decoratorSet);
//    }
}
