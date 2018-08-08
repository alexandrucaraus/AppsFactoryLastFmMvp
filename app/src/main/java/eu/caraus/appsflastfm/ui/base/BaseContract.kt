package eu.caraus.appsflastfm.ui.base

/**
 *
 *  BaseContract - defines base contract for MPV design pattern,
 *  a concrete implementation will implement this contract
 *
 */

interface BaseContract {

    interface BasePresenter<T : BaseContract.BaseView> {

        fun onViewAttached(view: T)
        fun onViewDetached(detach: Boolean)

    }

    interface BaseView

}