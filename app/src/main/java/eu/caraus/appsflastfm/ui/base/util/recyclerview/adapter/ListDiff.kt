package eu.caraus.appsflastfm.ui.base.util.recyclerview.adapter

class ListDiff<Item> {

    var insert : ( index : Int, type : Item ) -> Unit = {_,_->}
    var update : ( index : Int, type : Item ) -> Unit = {_,_->}
    var delete : ( index : Int, type : Item ) -> Unit = {_,_->}

    fun diff( oldList: List<Item>, newList: List<Item> ) {


        newList.minus( oldList).forEachIndexed { _ ,item ->
            insert( newList.indexOf(item),item )
        }

        oldList.minus( newList).forEachIndexed { _, item ->
            delete( oldList.indexOf(item), item )
        }

        if( newList.minus( oldList).isEmpty()
                && oldList.minus(newList).isEmpty()
                    && newList.isNotEmpty() ){
            newList.forEachIndexed {
                index, item ->

                item?.let {
                    if( it!! ==  oldList[index]){
                        update( index, it )
                    }
                }
            }
        }
    }

}