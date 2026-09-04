package Game.entities.division.games.enigmaGame;

import Collections.exceptions.EmptyCollectionException;
import Collections.list.ArrayUnorderedList;
import Collections.queue.LinkedQueue;
import Collections.queue.QueueADT;
import Game.api.division.enigmaGame.IEnigma;
import Game.api.division.enigmaGame.IEnigmaStrategy;
import Game.exceptions.division.NonEnigmaAvailableException;
import Game.exceptions.division.NullEnigmaException;

import java.util.Random;


/**
 * Strategy implementation that manages a rotating queue of enigmas.
 * <p>
 * Enigmas are stored in a queue and can be shuffled. Each time
 * an enigma is requested, it is dequeued and enqueued again, so
 * enigmas are reused in a cyclic manner.
 * </p>
 */
public class EnigmaStrategy implements IEnigmaStrategy {

    /** Queue of available enigmas. */
    private static QueueADT<IEnigma> list;

    /**
     * Creates a new {@code EnigmaStrategy} with an empty queue.
     */
    public EnigmaStrategy() {
        list = new LinkedQueue<>();
    }

    /**
     * Returns the next enigma in the queue and re-enqueues it.
     *
     * @return an {@link IEnigma} from the queue
     * @throws NonEnigmaAvailableException if no enigmas are available
     */
    @Override
    public IEnigma getEnigma() throws NonEnigmaAvailableException {
        try {
            IEnigma enigma = list.dequeue();
            list.enqueue(enigma);
            return enigma;
        } catch (EmptyCollectionException e) {
            throw new NonEnigmaAvailableException();
        }
    }

    /**
     * Adds a new enigma to the strategy queue and reshuffles the collection.
     *
     * @param enigma the enigma to add
     * @throws NullEnigmaException if the enigma is {@code null}
     */
    @Override
    public void addEnigma(IEnigma enigma) throws NullEnigmaException {
        if(enigma == null) {
            throw new NullEnigmaException();
        }

        list.enqueue(enigma);
        this.shuffleEnigmas();
    }

    /**
     * Randomly shuffles the order of the enigmas in the internal queue.
     */
    private void shuffleEnigmas() {
        ArrayUnorderedList<IEnigma> tempList = new ArrayUnorderedList<>();
        Random rand = new Random();

        while (!list.isEmpty()) {
            try {
                tempList.addToRear(list.dequeue());
            } catch (EmptyCollectionException e) {
                break;
            }
        }

        int size = tempList.size();
        IEnigma[] enigmas = new IEnigma[size];
        int idx = 0;
        for (IEnigma e : tempList) {
            enigmas[idx++] = e;
        }

        for (int i = 0; i < size; i++) {
            int j = rand.nextInt(size);
            IEnigma aux = enigmas[i];
            enigmas[i] = enigmas[j];
            enigmas[j] = aux;
        }

        for (IEnigma e : enigmas) {
            list.enqueue(e);
        }
    }

}
