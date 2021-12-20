package de.othr.eerben.erbenairports.backend.data.entities;

import java.io.Serializable;

public abstract class SingleIdEntity<K extends Comparable> implements Serializable, Comparable<SingleIdEntity> {
    public abstract K getID();

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        SingleIdEntity<K> otherSingleIdObject = (SingleIdEntity<K>) obj;

        return getID() != null ? getID().equals(otherSingleIdObject.getID()) : otherSingleIdObject.getID() == null;
    }

    @Override
    public int compareTo(SingleIdEntity other) {
        return this.getID().compareTo(other.getID());
    }
}