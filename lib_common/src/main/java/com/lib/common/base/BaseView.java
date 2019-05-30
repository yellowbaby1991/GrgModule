package com.lib.common.base;

/**
 * 规定View必须要实现setPresenter方法，则View中保持对Presenter的引用
 */
public interface BaseView<T> {

    void setPresenter(T presenter);

    void showLoding();

    void hodeLoding();

}
