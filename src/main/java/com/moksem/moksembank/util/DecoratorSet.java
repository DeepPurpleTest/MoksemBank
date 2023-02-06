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
}
