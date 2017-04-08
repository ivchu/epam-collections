package ru.epam.training;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

@FixMethodOrder
public class CustomHashMapTest {
    private Map<Integer, String> m;

    @Before
    public void init() {
        m = new CustomHashMap<>();
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

    @Test
    public void testThatWeCanPutNullValue() {
        m.put(5, null);
    }


    @Test
    public void testThatWECanGetValueByKey() {
        String value = "value";
        m.put(1, value);
        assertEquals(value, m.get(1));
    }

    @Test
    public void testThatWeCanGetNullKeyElement() {
        String value = null;
        m.put(1, value);
        assertEquals(value, m.get(1));
    }

    @Test
    public void testThatMapCanPutPairWithKeyThatAlreadyPresentedAndGetPreviousValue() {
        String value = "ss1";
        m.put(1, value);
        assertEquals(value, m.put(1, "ss2"));
    }

    @Test
    public void testThatMapCanContainsKeysWithSameHashCode() {
        String value1 = "ss1";
        String value2 = "ss2";
        m.put(1, value1);
        m.put(17, value2);
        m.containsKey(1);
        m.containsKey(17);
    }


    @Test
    public void testContainsValueMethodWorksProperlyForKeysWithDiffHash() {
        String value1 = "ss1";
        String value2 = "ss2";
        m.put(1, value1);
        m.put(2, value2);
        assertTrue(m.containsValue(value1));
        assertTrue(m.containsValue(value2));
    }

    @Test
    public void testContainsValueMethodWorksProperlyForKeysWithSameHash() {
        String value1 = "ss1";
        String value2 = "ss2";
        String value3 = null;
        String value4 = "ss4";
        m.put(1, value1);
        m.put(17, value2);
        m.put(33, value3);
        m.put(49, value4);
        assertTrue(m.containsValue(value1));
        assertTrue(m.containsValue(value2));
        assertTrue(m.containsValue(value3));
        assertTrue(m.containsValue(value4));
    }


    @Test
    public void testContainsValueMethodWorksProperlyOnNullInputValue() {
        String value1 = null;
        String value2 = "ss2";
        m.put(1, value1);
        m.put(17, value2);
        assertTrue(m.containsValue(value1));
        assertTrue(m.containsValue(value2));
    }


    @Test
    public void testThatMapCalculateItsSizeProperlyForPuttingSomeElements() {
        int expectedSize = 50;
        for (int i = 0; i < 50; i++) {
            m.put(i, "ss" + i);
        }
        assertTrue(expectedSize == m.size());
    }

    @Test
    public void testThatForMultipleKeysWithTheSameHashIfWePutNewValueWithOldKeyOnlyValueWillBeChanged() {
        int expectedSize = 3;
        m.put(1, "ss1");
        m.put(17, "ss17");
        m.put(33, "ss33");
        m.put(17, "ss172");
        assertTrue(m.size() == expectedSize);
    }

    @Test
    public void testThatWeCanGetCorrectElementWithKeyWhichHashHaveMultipleElementsInBucket() {
        String value1 = "ss1";
        String value2 = "ss17";
        String value3 = "ss33";
        m.put(1, value1);
        m.put(17, value2);
        m.put(33, value3);
        assertEquals(value1, m.get(1));
        assertEquals(value2, m.get(17));
        assertEquals(value3, m.get(33));
    }


    @Test
    public void testThatRemoveMethodNotDeletingWholeLLFromBucket() {
        String value1 = "ss1";
        String value2 = "ss17";
        String value3 = "ss33";
        m.put(1, value1);
        m.put(17, value2);
        m.put(33, value3);
        m.remove(33);
        assertTrue(m.containsKey(1));
        assertTrue(m.containsKey(17));
        assertTrue(m.containsValue(value1));
        assertTrue(m.containsValue(value2));
        assertFalse(m.containsKey(33));
        assertFalse(m.containsValue(value3));

    }

    @Test
    public void testThatRemoveMethodReturnsPreviousValue() {
        String value1 = "ss1";
        m.put(1, value1);
        assertEquals(value1, m.remove(1));
    }

    @Test
    public void testThatRemoveMethodReturnsNullIfNoSuchKeyInMap() {
        assertEquals(null, m.remove(1));
    }

    @Test
    public void testThatRemoveMethodWorksWellWithNullKey() {
        String value1 = "ss1";
        String value2 = "ss17";
        String value3 = "ss33";
        m.put(1, value1);
        m.put(null, value2);
        m.put(33, value3);
        assertEquals(value2, m.remove(null));
    }

    @Test
    public void testThatRemoveMethodChangingSizeProperlyRemovingInRightOrder() {
        int putAmount = 50;
        int removeAmount = 40;
        int expectedSize = putAmount - removeAmount;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        for (int i = 0; i < removeAmount; i++) {
            m.remove(i);
        }
        assertTrue(expectedSize == m.size());

    }

    @Test
    public void testThatRemoveMethodChangingSizeProperlyRemovingInBackOrder() {
        int putAmount = 50;
        int removeAmount = 40;
        int expectedSize = putAmount - removeAmount;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        for (int i = removeAmount - 1; i >= 0; i--) {
            m.remove(i);
        }
        assertTrue(expectedSize == m.size());

    }

    @Test
    public void testThatRemoveMethodResetsSizeToZero() {
        m.put(1, "ss");
        m.clear();
        assertEquals(0, m.size());
    }

    @Test
    public void testThatAfterRemoveMethodAppliedAllBucketsAreNulls() {
        int putAmount = 34;
        int default_capacity = 16;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        m.clear();
        for (int i = 0; i < default_capacity; i++) {
            assertEquals(null, m.get(i));
        }
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
    public void testThatKeySetIteratorCanRemoveKeys() {
        int putAmount = 34;
        int removeAmount = putAmount / 2;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        Set<Integer> setOfKeys = m.keySet();
        Iterator iterator = setOfKeys.iterator();
        int leftInSet = putAmount - removeAmount;
        for (int i = 0; i < removeAmount; i++) {
            iterator.remove();
        }
        assertTrue(leftInSet == m.size());
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
    public void testThatEntrySetIteratorCanRemoveEntries() {
        int putAmount = 34;
        int removeAmount = putAmount / 2;
        for (int i = 0; i < putAmount; i++) {
            m.put(i, "ss" + i);
        }
        Set<Map.Entry<Integer, String>> entries = m.entrySet();
        Iterator iterator = entries.iterator();
        int leftInSet = putAmount - removeAmount;
        for (int i = 0; i < removeAmount; i++) {
            iterator.remove();
        }
        assertTrue(leftInSet == m.size());
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

    @Test
    public void testThatWeCanUseNullAsKey(){
        String value = "ss1";
        m.put(null, value);
        assertEquals(value, m.get(null));
    }
}
