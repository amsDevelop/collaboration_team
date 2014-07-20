package com.sinopec.drawtool;

import java.util.EventListener;

/**
 * 
 * @author ropp gispace@yeah.net
 *
 */
public interface DrawEventListener extends EventListener {

	void handleDrawEvent(DrawEvent event);
	void clear();
}
