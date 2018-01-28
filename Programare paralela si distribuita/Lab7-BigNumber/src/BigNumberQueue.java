import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gobi on 12/7/2017.
 */
public class BigNumberQueue extends LinkedBlockingQueue<Tuple<Integer, Integer>> {

    public boolean isDone;

}
