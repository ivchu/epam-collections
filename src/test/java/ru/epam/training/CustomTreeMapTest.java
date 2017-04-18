package ru.epam.training;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@FixMethodOrder
public class CustomTreeMapTest {

    private Map<Integer, String> m;

    @Before
    public void init() {
        m = new CustomTreeMap<>();
    }

    @Test
    public void testThatWeCanCreate() {

        assertThat(m, is(notNullValue()));

    }

    @Test
    public void testThatNewMapIsEmpty() {
        assertThat(m.isEmpty(), is(true));
    }

    @Test
    public void testThatOnNewMapContainKeyMethodReturnFalseForAnyObject() {
        assertThat(m.containsKey(new Integer(1)), is(false));
    }

    @Test
    public void testThatWeCanPutKeyValuePairAndCanCheckIt() {
        m.put(new Integer(3), "abc");
        assertThat(m.containsKey(3), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void testThatWeCantPutNullKey() {
        m.put(null, "abc");
    }

    @Test
    public void testThatWeCanPutNullValue() {
        m.put(1, null);
        assertThat(m.containsKey(1), is(true));
    }

    @Test
    public void testThatMapCanPutPairWithKeyThatAlreadyPresented() {

        String oldValue = "aaaa";
        String newValue = "bbbb";

        m.put(1, oldValue);
        m.put(1, newValue);

        assertFalse(m.containsValue(oldValue));
        assertTrue(m.containsValue(newValue));
    }

    @Test
    public void testThatIfWePutNewValueOnExistingKeyPreviousValueWillBeReturned() {
        String oldValue = "aaaa";
        String newValue = "bbbb";

        m.put(1, oldValue);
        String returnedValue = m.put(1, newValue);

        assertThat(oldValue, is(equalTo(returnedValue)));
    }

    @Test(expected = NullPointerException.class)
    public void testThatContainsKeyMethodThrowsExceptionOnNullKey() {
        m.containsKey(null);
    }

    @Test(expected = ClassCastException.class)
    public void testThatContainsKeyMethodThrowsExceptionOnWrongKeyClass() {
        m.put(1, "");
        m.containsKey(new String(""));
    }

    @Test
    public void testContainsValueMethodWorksProperlyOn() {
        String value = "aaaa";

        m.put(1, value);

        assertTrue(m.containsValue(value));
    }

    @Test
    public void testContainsValueMethodWorksProperlyOnNullInputValue() {
        String value = "aaaa";

        m.put(1, value);

        assertFalse(m.containsValue(null));
    }

    @Test
    public void testThatWeCanPut10DifferentKeysInMap() {
        IntStream.range(1, 10).forEach(
                i -> m.put(i, String.valueOf(i))
        );

        IntStream.range(1, 10).forEach(
                i -> assertTrue(m.containsKey(i))
        );
    }

    @Test(expected = NullPointerException.class)
    public void testThatGetThrowsNPEIfKeyNull() {
        m.get(null);
    }

    @Test(expected = ClassCastException.class)
    public void testThatGetThrowsCCEForWrongClassKey() {
        fillTreeMap();
        m.get(new Object());
    }

    @Test
    public void testThatGetWorksProperlyReturnsCorrectValueMultipleTimes() {
        fillTreeMap();
        IntStream.range(1, 10).forEach(
                i -> assertThat(m.get(i), is("" + i))
        );
    }

    @Test
    public void testThatGetReturnsNullValueIfNoElementWithSuchKey() {
        assertThat(m.get(100), is(nullValue()));
    }

    @Test
    public void testThatIsEmptyWorksCorrectly() {
        assertTrue(m.isEmpty());
        fillTreeMap();
        assertFalse(m.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testThatMethodRemoveThrowsNPEForNullParameter() {
        m.remove(null);
    }

    @Test(expected = ClassCastException.class)
    public void testThatMethodRemoveThrowsCCEIfForWrongKeyClass() {
        m.remove(new Object());
    }

    @Test
    public void testThatMethodRemoveReturnsNullIfNoSuchKeyInMap() {
        fillTreeMap();
        assertThat(m.remove(21), is(nullValue()));
    }

    @Test
    public void testThatMethodRemoveReturnsRemovedValue() {
        fillTreeMap();
        assertThat(m.remove(1), is("1"));
    }


    @Test
    public void testThatMethodRemoveRemovesNode() {
        fillTreeMap();
        m.remove(1);
        assertFalse(m.containsKey(1));
    }

    @Test
    public void testThatMapCalculateItsSizeProperly() {
        int size = fillTreeMap();
        assertEquals(size, m.size());
        m.remove(0);
        size--;
        m.remove(1);
        size--;
        m.remove(2);
        size--;
        assertEquals(size, m.size());
    }

    @Test
    public void testThatClearMethodResetsSizeToZero(){
        fillTreeMap();
        m.clear();
        assertEquals(0, m.size());
    }

    @Test
    public void testThatClearMethodRemovesValues(){
        fillTreeMap();
        m.clear();
        IntStream.range(1, 10).forEach(
                i -> assertThat(m.containsValue("" + i), is(false))
        );
    }

    @Test
    public void testThatKeySetMethodReturnsAllKeys() {
        int putAmount = 34;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        Set<Integer> setOfKeys = m.keySet();
        for (int i = 0; i < putAmount; i++) {
            assertTrue(setOfKeys.contains(i));
        }
    }

    @Test
    public void testThatKeySetMethodDontHaveMoreKeysThenInMap() {
        int putAmount = 34;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        Set<Integer> setOfKeys = m.keySet();
        assertTrue(m.size() == setOfKeys.size());
    }

    @Test
    public void testThatKeySetIteratorWorksWell() {
        int putAmount = 34;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        Set<Integer> setOfKeys = m.keySet();
        Iterator iterator = setOfKeys.iterator();
        int iteratedAmount = 0;
        while (iterator.hasNext()) {
            Object it = iterator.next();
            assertTrue(setOfKeys.contains(it));
            iteratedAmount++;
        }
        assertEquals(iteratedAmount, setOfKeys.size());
    }

    @Test
    public void testThatValuesMethodReturnsAllValues() {
        int putAmount = 34;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        Collection<String> valueCollection = m.values();
        for (int i = 0; i < putAmount; i++) {
            assertTrue(valueCollection.contains("ss" + i));
        }
    }

    @Test
    public void testThatValuesMethodDontHaveMoreValuesThenInMap() {
        int putAmount = 100;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        Collection<String> valueCollection = m.values();
        assertTrue(m.size() == valueCollection.size());
    }

    @Test
    public void testThatValuesIteratorWorksWell() {
        int putAmount = 34;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        Collection<String> valueCollection = m.values();
        Iterator iterator = valueCollection.iterator();
        int iteratedAmount = 0;
        while (iterator.hasNext()) {
            Object it = iterator.next();
            assertTrue(valueCollection.contains(it));
            iteratedAmount++;
        }
        assertEquals(iteratedAmount, valueCollection.size());
    }

    @Test
    public void testThatEntrySetMethodReturnsAllEntries() {
        HashMap<Integer, String> testingMap = new HashMap<>();
        int putAmount = 34;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
            testingMap.put(i, "ss" + i);
        }
        Set<Map.Entry<Integer, String>> entries = m.entrySet();
        for (Map.Entry<Integer, String> mustBeIn : testingMap.entrySet()) {
            assertTrue(entries.contains(mustBeIn));
        }
    }

    @Test
    public void testThatEntrySetMethodContainsReturnsFalseIfNoSuchEntry() {
        HashMap<Integer, String> testingMap = new HashMap<>();
        int putAmount = 34;
        int notInMapEntries = 80;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        for (int i = putAmount; i < notInMapEntries; i++) {
            testingMap.put(i, "ss" + i);
        }
        Set<Map.Entry<Integer, String>> entries = m.entrySet();
        for (Map.Entry<Integer, String> mustntBeIn : testingMap.entrySet()) {
            assertFalse(entries.contains(mustntBeIn));
        }
        assertFalse(entries.contains(1));
    }

    @Test
    public void testThatEntrySetMethodDontHaveMoreEntriesThenInMap() {
        int putAmount = 34;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        Set<Map.Entry<Integer, String>> entries = m.entrySet();
        assertTrue(m.size() == entries.size());
    }

    @Test
    public void testThatEntrySetIteratorWorksWell() {
        int putAmount = 34;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        Set<Map.Entry<Integer, String>> entries = m.entrySet();
        Iterator iterator = entries.iterator();
        int iteratedAmount = 0;
        while (iterator.hasNext()) {
            Object it = iterator.next();
            assertTrue(entries.contains(it));
            iteratedAmount++;
        }
        assertEquals(iteratedAmount, entries.size());
    }

    @Test
    public void testThatPutAllInsertsAllElements() {
        HashMap<Integer, String> insertMap = new HashMap<>();
        int putAmount = 204;
        for (int i = 0; i < putAmount; i++) {
            insertMap.put(i, "ss" + i);
        }
        m.putAll(insertMap);
        for (Map.Entry mustBeIn : insertMap.entrySet()) {
            assertTrue(m.containsKey(mustBeIn.getKey()));
            assertTrue(m.containsValue(mustBeIn.getValue()));
        }
        assertEquals(insertMap.size(), m.size());
    }

    @Test(expected = NullPointerException.class)
    public  void testThatPutAllMethodThrowsNPEIfInputMapIsNull(){
        m.putAll(null);
    }



    private int fillTreeMap() {
        int size = 16;
        m.put(0, "0");
        m.put(10, "10");
        m.put(1, "1");
        m.put(20, "20");
        m.put(2, "2");
        m.put(90, "90");
        m.put(3, "3");
        m.put(4, "4");
        m.put(5, "5");
        m.put(9, "9");
        m.put(8, "8");
        m.put(7, "7");
        m.put(6, "6");
        m.put(80, "80");
        m.put(70, "70");
        m.put(60, "60");
        return size;
    }


}
