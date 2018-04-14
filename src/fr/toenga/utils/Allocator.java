package fr.toenga.utils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class Allocator
{
	
    private int				min;
    private int				max;
    private int				next;
    private Queue<Integer>	notAllocated;
    
    public Allocator(int min, int max)
    {
        this.notAllocated = new ConcurrentLinkedDeque<Integer>();
        this.min = min;
        this.max = max;
        this.next = min;
    }
    
    public int allocate()
    {
        if (getNotAllocated().size() > 0)
        {
            return getNotAllocated().poll();
        }
        
        if (getNext() == getMax() && getMin() < getMax())
        {
            throw new IllegalStateException("No more number to allocate.");
        }
        return this.next++;
    }
    
    public void free(int number)
    {
        assert getNext() > number && number > getMin();
        getNotAllocated().add(number);
    }
    
}
