package ru.epam.training;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CustomListsTest {

    private List<String> list;

    public CustomListsTest(List<String> list) {
        this.list = list;
    }

    @Parameterized.Parameters
    public static Collection<Object> data() {
        return Arrays.asList(new Object[]{
                new CustomArrayList(),
                new CustomLinkedList()
        });
    }

    @Before
    public void init() {
        list.clear();
    }

    @Test
    public void testThatNewListIsEmpty() {
        assertTrue(list.isEmpty());
    }

    @Test
    public void testThatListNotEmptyAfterAddingElement() {
        list.add("aaaa");
        assertThat(list.isEmpty(), is(false));
    }

    @Test
    public void testThatListContainsElementThatWeAddedBefore() {
        String value = "bbb";

        list.add(value);

        assertTrue(list.contains(value));
    }

    @Test
    public void testThatListNotContainsElementThatWasntAddedToList() throws Exception {
        list.add("fff");
        assertFalse(list.contains("ccc"));
    }

    @Test
    public void testThatListContainsNullIfItWasAdded() {

        list.add(null);

        assertTrue(list.contains(null));
    }

    @Test
    public void testThatListNotContainsNullIfItWasNotAdded() {
        list.add("fff");
        assertFalse(list.contains(null));
    }

    @Test
    public void testThatListsSizeIsDynamic() throws Exception {
        int size = 50;

        for (int i = 0; i < size; i++) {
            list.add(String.valueOf(i));
        }

        assertThat(list.size(), is(size));
    }

    @Test
    public void testThatWeCanGetElementByIndex() {

        fillList();
        list.add("aa1a");
        int index = list.size() - 1;
        assertThat(list.get(index), is(equalTo("aa1a")));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testThatWeCantGetElementByIndexMoreThenSize() throws Exception {

        fillList();

        list.get(list.size() + 1);
    }

    @Test
    public void testThatWeCanRemoveExistedElementFromList() throws Exception {
        fillList();

        list.remove("ssss");

        assertFalse("contains", list.contains("ssss"));
    }

    @Test
    public void testThatWeCanDeleteElementByIndex() throws Exception {
        fillList();
        String toRemove = list.get(1);
        String removed = list.remove(1);
        assertFalse(list.contains(removed));
        assertThat(removed, is(equalTo(toRemove)));
    }

    @Test
    public void testThatWeCanDeleteLastElement() throws Exception {
        fillList();

        int prevSize = list.size();

        list.remove(list.size() - 1);

        assertFalse(list.contains("aa4a"));
        assertThat(list.size(), is(equalTo(prevSize - 1)));
    }

    @Test
    public void testThatWeCantDeleteNonExistentElement() throws Exception {
        fillList();

        assertFalse(list.remove("sadasdasd"));
    }

    @Test
    public void testThatToArrayReturnsCorrectArray() {
        fillList();
        ArrayList<Object> tester = new ArrayList<>();
        for (Object obj : list.toArray()) {
            tester.add(obj);
        }
        assertTrue(tester.contains("aa0a"));
        assertTrue(tester.contains("aa1a"));
        assertTrue(tester.contains("aa2a"));
        assertTrue(tester.contains("ssss"));
        assertTrue(tester.contains("aa3a"));
        assertTrue(tester.contains("aa4a"));
        assertEquals(tester.size(), list.size());
    }

    @Test
    public void testThatToArrayReturnsZeroLengthObjectArrayFromEmptyList() {
        assertEquals(new Object[0], list.toArray());
    }

    @Test
    public void testThatSetMethodReturnsValue() {
        fillList();
        String value = list.get(2);
        String valueForSet = "ggg";
        assertEquals(value, list.set(2, valueForSet));
        assertEquals(valueForSet, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testThatMethodSetThrowsOutOfBoundIfIndexIsWrong() {
        list.set(15, " sdfa");
    }

    @Test
    public void testThatAddAllWorksWell() {
        ArrayList<String> tester = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            tester.add("sss" + i);
        }
        list.addAll(tester);
        for (String s : tester) {
            assertTrue(list.contains(s));
        }
        assertEquals(tester.size(), list.size());
    }

    @Test(expected = NullPointerException.class)
    public void testThatAddAllThrowsNPEForNullCollection() {
        list.addAll(null);
    }

    @Test
    public void testThatAddAllWithIndexWorksWell() {
        fillList();
        int sizeBeforeAdding = list.size();
        ArrayList<String> tester = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            tester.add("sss" + i);
        }
        list.addAll(3, tester);
        assertEquals(tester.get(0), list.get(3));
        assertEquals(tester.get(49), list.get(52));
        for (String s : tester) {
            assertTrue(list.contains(s));
        }
        assertEquals(tester.size() + sizeBeforeAdding, list.size());
    }

    @Test(expected = NullPointerException.class)
    public void testThatAddAllWithIndexThrowsNPEForNullCollection() {
        fillList();
        list.addAll(3, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testThatAddAllWithIndexThrowsIOOBException() {
        list.addAll(100, null);
    }

    @Test
    public void testThatAddAllReturnsFalseIfCollectionForAddWasEmpty() {
        assertFalse(list.addAll(0, new ArrayList<>()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testThatAddWithIndexThrowsIOOBException() {
        list.add(55, "fsd");
    }

    @Test
    public void testThatAddWithIndexWorksWell() {
        fillList();
        int expectedSize = list.size() + 1;
        String valueOn2 = list.get(2);
        String valueOn3 = list.get(3);
        String value = "sob";
        list.add(3, value);
        assertEquals(expectedSize, list.size());
        assertEquals(value, list.get(3));
        assertEquals(valueOn2, list.get(2));
        assertEquals(valueOn3, list.get(4));
    }

    @Test(expected = NullPointerException.class)
    public void testThatParametrizedToArrayThrowsNPEIfParamIsNull() {
        list.toArray(null);
    }

    @Test
    public void testThatParametrizedToArrayReturnsZeroLengthObjectArrayFromEmptyList() {
        assertEquals(new String[0], list.toArray(new String[0]));
    }

    @Test
    public void testThatParametrizedToArrayReturnsCorrectType() {
        fillList();
        ArrayList<String> tester = new ArrayList<>();
        String[] array = new String[list.size()];
        list.toArray(array);
        for (String str : array) {
            tester.add(str);
        }
        assertTrue(tester.contains("aa0a"));
        assertTrue(tester.contains("aa1a"));
        assertTrue(tester.contains("aa2a"));
        assertTrue(tester.contains("ssss"));
        assertTrue(tester.contains("aa3a"));
        assertTrue(tester.contains("aa4a"));
        assertEquals(tester.size(), list.size());
    }

    @Test
    public void testThatIndexOfReturnsCorrectIndex() {
        fillList();
        int Index = 3;
        assertEquals(Index, list.indexOf(list.get(3)));
    }

    @Test
    public void testThatIndexOfReturnsMinusOneIfNoSuchObject() {
        fillList();
        int correctIndex = -1;
        assertEquals(correctIndex, list.indexOf("MFWrong"));
    }

    @Test
    public void testThatIndexOfWorksCorrectlyWithNull() {
        fillList();
        int correctIndex = 2;
        assertEquals(correctIndex, list.indexOf(null));
    }

    @Test
    public void testThatLastIndexOfReturnsCorrectIndex() {
        fillList();
        list.add("ssss");
        int correctIndex = list.size() - 1;
        assertEquals(correctIndex, list.lastIndexOf("ssss"));
    }

    @Test
    public void testThatLastIndexOfReturnsMinusOneIfNoSuchObject() {
        fillList();
        int correctIndex = -1;
        assertEquals(correctIndex, list.lastIndexOf("MFWrong"));
    }

    @Test
    public void testThatLastIndexOfWorksCorrectlyWithNull() {
        fillList();
        list.add(null);
        int correctIndex = list.size() - 1;
        assertEquals(correctIndex, list.lastIndexOf(null));
    }

    @Test
    public void testRemoveMethodRemovesNull() {
        fillList();
        assertTrue(list.remove(null));
    }

    @Test
    public void testLastIndexOFNotRemovesNullIfCollectionHaveNotIt() {
        list.add("hjgj");
        assertEquals(-1, list.lastIndexOf(null));
    }

    @Test
    public void testThatListIteratorGoesThrowAllCollectionForward() {
        fillList();
        ArrayList<String> putWithIterHere = new ArrayList<>();
        ListIterator<String> iterator = list.listIterator();
        while (iterator.hasNext()) {
            putWithIterHere.add(iterator.next());
        }
        for (int i = 0; i < list.size(); i++) {
            assertTrue(putWithIterHere.contains(list.get(i)));
        }
    }

    @Test
    public void testThatListIteratorGoesThrowAllCollectionBackward() {
        fillList();
        ArrayList<String> putWithIterHere = new ArrayList<>();
        ListIterator<String> iterator = list.listIterator(list.size());
        while (iterator.hasPrevious()) {
            putWithIterHere.add(iterator.previous());
        }
        for (int i = 0; i < list.size(); i++) {
            assertTrue(putWithIterHere.contains(list.get(i)));
        }
    }

    @Test
    public void testThatListIteratorCanSetElementOnCorrectPosition() {
        fillList();
        ListIterator<String> iterator = list.listIterator();
        int setPos = 3;
        for (int i = 0; i <= setPos; i++) {
            iterator.next();
        }
        iterator.set("MF");
        assertTrue(list.contains("MF"));
        assertEquals(setPos, list.indexOf("MF"));
    }

    @Test
    public void testThatListIteratorCanAddElement() {
        fillList();
        ListIterator<String> iterator = list.listIterator();
        int justForFun = 3;
        int prevSize = list.size();
        for (int i = 0; i < justForFun; i++) {
            iterator.next();
        }
        iterator.add("MF");
        assertTrue(list.contains("MF"));
        assertEquals(prevSize + 1, list.size());
    }

    @Test
    public void testThatListIteratorCanRemoveElement() {
        fillList();
        ListIterator<String> iterator = list.listIterator();
        int justForFun = 3;
        int prevSize = list.size();
        for (int i = 0; i <= justForFun; i++) {
            iterator.next();
        }
        String value = list.get(3);
        iterator.remove();
        assertFalse(list.contains(value));
        assertEquals(prevSize - 1, list.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testThatListIteratorHasNextThrowsNSEEForNext() {
        ListIterator<String> iterator = list.listIterator();
        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThatListIteratorHasNextThrowsNSEEForPrevious() {
        ListIterator<String> iterator = list.listIterator();
        iterator.previous();
    }

    @Test(expected = IllegalStateException.class)
    public void testThatListIteratorHasNextThrowsISEForSet() {
        ListIterator<String> iterator = list.listIterator();
        iterator.set("s");
    }

    @Test(expected = IllegalStateException.class)
    public void testThatListIteratorHasNextThrowsISEForRemove() {
        ListIterator<String> iterator = list.listIterator();
        iterator.remove();
    }

    @Test(expected = NullPointerException.class)
    public void testThatContainAllThrowsNPEIfParameterIsNull() {
        fillList();
        list.containsAll(null);
    }

    @Test
    public void testThatContainAllWorksWell() {
        fillList();
        ArrayList<String> notContainsInList = new ArrayList<>();
        notContainsInList.add(null);
        notContainsInList.add("aa1a");
        notContainsInList.add("hhh");
        notContainsInList.add("sad");
        notContainsInList.add("hhh");
        notContainsInList.add("aa2a");
        ArrayList<String> containsInList = new ArrayList<>();
        containsInList.add(null);
        containsInList.add("aa1a");
        containsInList.add("hhh");
        containsInList.add("hhh");
        containsInList.add("aa2a");
        assertTrue(list.containsAll(containsInList));
        assertFalse(list.containsAll(notContainsInList));
    }

    @Test
    public void testThatRemoveAllWorksWell() {
        fillList();
        ArrayList<String> containsInList = new ArrayList<>();
        containsInList.add(null);
        containsInList.add("aa1a");
        containsInList.add("hhh");
        containsInList.add("hhh");
        containsInList.add("aa2a");
        ArrayList<String> notContainsInList = new ArrayList<>();
        notContainsInList.add("sad");
        notContainsInList.add("sa1d");
        notContainsInList.add("sad2");
        assertFalse(list.removeAll(notContainsInList));
        assertTrue(list.removeAll(containsInList));
        assertFalse(list.contains("aa2a"));
        assertFalse(list.contains("aa1a"));
        assertTrue(list.contains("hhh"));
        assertTrue(list.contains(null));
    }

    @Test(expected = NullPointerException.class)
    public void testThatRemoveAllThrowsNPEIfParameterIsNull() {
        fillList();
        list.removeAll(null);
    }

    @Test
    public void testThatRetainAllRemoveElements() {
        fillList();
        ArrayList<String> notContainsInList = new ArrayList<>();
        notContainsInList.add("sad");
        notContainsInList.add("sa1d");
        notContainsInList.add(null);
        notContainsInList.add("aa1a");
        notContainsInList.add("hhh");
        notContainsInList.add("hhh");
        notContainsInList.add("aa2a");
        notContainsInList.add("sad2");
        assertTrue(list.retainAll(notContainsInList));
        assertFalse(list.contains("sss"));
        assertFalse(list.contains("aa3a"));
        assertTrue(list.contains("hhh"));
        assertTrue(list.contains(null));
    }

    @Test
    public void testThatRetainAllReturnsFalseIfNorRemovedAnyThing() {
        fillList();
        ArrayList<String> containsInList = new ArrayList<>();
        containsInList.addAll(list);
        assertFalse(list.retainAll(containsInList));
        assertTrue(list.contains("ssss"));
        assertTrue(list.contains("aa3a"));
        assertTrue(list.contains("hhh"));
        assertTrue(list.contains(null));
    }

    @Test(expected = NullPointerException.class)
    public void testThatRetainAllThrowsNPEIfParameterIsNull() {
        fillList();
        list.retainAll(null);
    }

    private void fillList() {
        list.add("hhh");
        list.add("aa0a");
        list.add(null);
        list.add("aa1a");
        list.add("hhh");
        list.add("hhh");
        list.add("aa2a");
        list.add("hhh");
        list.add("ssss");
        list.add(null);
        list.add("aa3a");
        list.add("hhh");
        list.add("aa4a");
    }
}
