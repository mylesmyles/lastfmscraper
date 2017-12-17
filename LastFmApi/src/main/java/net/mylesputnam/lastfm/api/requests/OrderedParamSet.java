package net.mylesputnam.lastfm.api.requests;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class OrderedParamSet implements Iterable<RequestParam> {
	private final LinkedHashMap<String, RequestParam> internalMap;
	
	public static OrderedParamSet combine(OrderedParamSet defaultParams, OrderedParamSet overrideParams) {
		OrderedParamSet combinedSets = new OrderedParamSet(defaultParams.asList());
		combinedSets.addAll(overrideParams.asList());
		return combinedSets;
	}
	
	public OrderedParamSet() {
		this.internalMap = new LinkedHashMap<>();
	}
	
	public OrderedParamSet(Collection<RequestParam> params) {
		this();
		this.addAll(params);
	}
	
	public void add(RequestParam param) {
		if(param != null) {
			this.internalMap.put(param.key, param);
		}
	}
	
	public void addAll(Collection<RequestParam> params) {
		if(params == null) {
			return;
		}
		
		for(RequestParam param : params) {
			this.add(param);
		}
	}
	
	public List<RequestParam> asList() {
		return new LinkedList<>(internalMap.values());
	}

	@Override
	public Iterator<RequestParam> iterator() {
		final Iterator<String> internalIterator = internalMap.keySet().iterator();
		return new Iterator<RequestParam>() {
			@Override
			public boolean hasNext() {
				return internalIterator.hasNext();
			}

			@Override
			public RequestParam next() {
				return internalMap.get(internalIterator.next());
			}
		};
	}
}
