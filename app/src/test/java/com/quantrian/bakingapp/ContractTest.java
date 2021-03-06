package com.quantrian.bakingapp;

import android.provider.BaseColumns;

import com.quantrian.bakingapp.data.ContractIngredient;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Borrowed from ud851-Exerises/Lesson07-Waitlist to get the hang of local unit testing.
 */

public class ContractTest {


    @Test
    public void inner_class_exists() throws Exception {
        Class[] innerClasses = ContractIngredient.class.getDeclaredClasses();
        assertEquals("There should be 1 Inner class inside the contract class", 1, innerClasses.length);
    }

    @Test
    public void inner_class_type_correct() throws Exception {
        Class[] innerClasses = ContractIngredient.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.length);
        Class entryClass = innerClasses[0];
        assertTrue("Inner class should implement the BaseColumns interface", BaseColumns.class.isAssignableFrom(entryClass));
        assertTrue("Inner class should be final", Modifier.isFinal(entryClass.getModifiers()));
        assertTrue("Inner class should be static", Modifier.isStatic(entryClass.getModifiers()));
    }

    @Test
    public void inner_class_members_correct() throws Exception {
        Class[] innerClasses = ContractIngredient.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.length);
        Class entryClass = innerClasses[0];
        Field[] allFields = entryClass.getDeclaredFields();
        //assertEquals("There should be exactly 4 String members in the inner class", 4, allFields.length);
        for (Field field : allFields) {
            //assertTrue("All members in the contract class should be Strings", field.getType()==String.class);
            assertTrue("All members in the contract class should be final", Modifier.isFinal(field.getModifiers()));
            assertTrue("All members in the contract class should be static", Modifier.isStatic(field.getModifiers()));
        }
    }

}