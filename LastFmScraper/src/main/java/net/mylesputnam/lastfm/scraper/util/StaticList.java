package net.mylesputnam.lastfm.scraper.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StaticList<E> implements Iterable<E> {
	private final List<E> underlyingList;
	
	public static <E> StaticList<E> create(Collection<E> sourceList) {
		return new StaticList<E>(sourceList);
	}
	
	private StaticList(Collection<E> sourceList) {
		this.underlyingList = new ArrayList<>();
		
		if(sourceList == null || sourceList.isEmpty()) {
			return;
		}
		
		for(E element : sourceList) {
			if(element == null)
				continue;
			
			this.underlyingList.add(element);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private final Iterator<E> baseListIterator = underlyingList.iterator();
			
			@Override
			public boolean hasNext() {
				return baseListIterator.hasNext();
			}

			@Override
			public E next() {
				return baseListIterator.next();
			}
		};
	}
}
