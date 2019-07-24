package de.hswt.filehandler.api;

import java.nio.file.Path;
import java.util.*;

public class MainReader<C> {

	private Map<String, Reader<C>> readers;

	private Inspector<C> inspector;

	public MainReader(Inspector<C> inspector, Collection<? extends Reader<C>> readers) {
		this.inspector = inspector;
		this.readers = new HashMap<>();
		setReaders(readers);
	}

	public Object parseFile(Path path, Class<?> clazz, String id) {
		Reader<C> reader = readers.get(id);
		if (reader == null || Double.doubleToRawLongBits(
				inspector.canHandle(readers.get(id), path, clazz)) == Reader.CAN_NOT_HANDLE) {
			throw new IllegalArgumentException(
					"Invalid reader. Reader does not exist or cannot read the given file.");
		}
		return reader.getContent(path);
	}

	public String getFirstReaderID(Path path, Class<?> clazz) {

		List<String> ids = getReaderIDs(path, clazz);
		if (ids.isEmpty()) {
			return null;
		}
		return ids.get(0);
	}

	private List<String> getReaderIDs(Path path, Class<?> clazz) {
		if (path == null || clazz == null || !path.toFile().exists() || readers == null) {
			throw new IllegalArgumentException("The arguments must not be null or invalid");
		}

		Map<String, Double> capableReaders = new HashMap<>();

		capableReaders.putAll(inspector.getCapableReaders(readers, path, clazz));

		ValueComparator bvc = new ValueComparator(capableReaders);

		SortedMap<String, Double> sortedCapableReaders = new TreeMap<>(bvc);
		sortedCapableReaders.putAll(capableReaders);
		List<String> ids = new ArrayList<>();

		ids.addAll(sortedCapableReaders.keySet());

		return ids;
	}

	private void setReaders(Collection<? extends Reader<C>> readers) {
		this.readers.clear();
		if (readers == null) {
			throw new IllegalArgumentException("The argument readers must not be null.");
		}
		for (Reader<C> reader : readers) {
			if (reader != null) {
				this.readers.put(reader.getID(), reader);
			}
		}
	}

	private class ValueComparator implements Comparator<String> {

		Map<String, Double> base;

		ValueComparator(Map<String, Double> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with
		// equals.
		@Override
		public int compare(String a, String b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			} // returning 0 would merge keys
		}
	}
}
