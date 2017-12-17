package net.mylesputnam.lastfm.api.requests;

public final class RequestParam {
	public final String key;
	public final String value;
	
	public static RequestParam create(String key, String value) {
		return new RequestParam(key, value);
	}
	
	public static RequestParam apiKey(String apiKey) {
		return RequestParam.create("api_key", apiKey);
	}
	
	public static RequestParam jsonFormat() {
		return RequestParam.create("format", "json");
	}
	
	public static RequestParam jsonCallback(String callback) {
		return RequestParam.create("callback", callback);
	}
	
	public static RequestParam method(String method) {
		return RequestParam.create("method", method);
	}
	
	public static RequestParam artist(String artist) {
		return RequestParam.create("artist", artist);
	}
	
	private RequestParam(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getParamString() {
		return key + "=" + value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestParam other = (RequestParam) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
