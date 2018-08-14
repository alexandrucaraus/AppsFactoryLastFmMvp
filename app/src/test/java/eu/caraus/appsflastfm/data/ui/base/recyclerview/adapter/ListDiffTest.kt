package eu.caraus.appsflastfm.data.ui.base.recyclerview.adapter

import eu.caraus.appsflastfm.ui.common.recyclerview.adapter.ListDiff
import org.junit.Assert.assertEquals

import org.junit.Test

class ListDiffTest {


    @Test
    fun insertTest() {

        val listDiff = ListDiff<String>()

        val oldList = mutableListOf<String>("o1","o2","o3","o4","o5","o6")
        val newList = mutableListOf<String>("o1","o2","n1","o3","o4","o5","o6")



        listDiff.insert = { i,o ->
            assertEquals( 2 ,i)
            assertEquals( o, "n1")
        }

        listDiff.diff( oldList, newList )
    }

    @Test
    fun deleteTest() {

        val listDiff = ListDiff<String>()

        val newList = mutableListOf<String>("o1","o2","o3","o4","o5","o6")
        val oldList = mutableListOf<String>("o1","o2","n1","o3","o4","o5","o6")

        listDiff.delete = { i,o ->
            assertEquals( 2 ,i)
            assertEquals( o, "n1")
        }

    }

    @Test
    fun updateTest(){

        val listDiff = ListDiff<String>()

        val oldList = mutableListOf<String>("o1","o2","o3","o4","o5")
        val newList = mutableListOf<String>("o1_","o2","o3","o4","o5")

        listDiff.update = { i,o ->
            assertEquals( 0, i)
            assertEquals( "o1_",o)
        }

    }

}