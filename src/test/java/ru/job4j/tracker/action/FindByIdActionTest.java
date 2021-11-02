package ru.job4j.tracker.action;

import org.junit.Test;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.output.StubOutput;
import ru.job4j.tracker.store.MemTracker;
import ru.job4j.tracker.store.Store;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindByIdActionTest {

    @Test
    public void whenExecuteThenSuccess() {
        Output out = new StubOutput();
        Store tracker = new MemTracker();
        Item item = new Item("Item");
        tracker.add(item);
        FindByIdAction action = new FindByIdAction(out);

        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn("0");
        action.execute(input, tracker);

        String ln = System.lineSeparator();
        assertThat(
                out.toString(),
                is(item + ln)
        );
    }
}
