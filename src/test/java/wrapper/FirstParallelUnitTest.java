package wrapper;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Execution(ExecutionMode.CONCURRENT)
public class FirstParallelUnitTest{
    public static List<Integer> order = new ArrayList<>();

    @Test
    public void first() throws Exception{
        order.add(1);
        System.out.println("FirstParallelUnitTest first() start => " + Thread.currentThread().getName());
        Thread.sleep(500);
        System.out.println("FirstParallelUnitTest first() end => " + Thread.currentThread().getName());
        order.add(2);
    }

    @Test
    public void second() throws Exception{
        order.add(3);
        System.out.println("FirstParallelUnitTest second() start => " + Thread.currentThread().getName());
        Thread.sleep(500);
        System.out.println("FirstParallelUnitTest second() end => " + Thread.currentThread().getName());
        order.add(4);
    }

    @AfterAll
    static void print(){
        System.out.println(Arrays.toString(order.toArray()));
    }
}