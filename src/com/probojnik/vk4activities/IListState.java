package com.probojnik.vk4activities;

public interface IListState {
	String url(Object... par);
	boolean next(Object... par);
	boolean prev(Object... par);
}
