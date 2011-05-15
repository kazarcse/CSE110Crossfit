package edu.ucsd.cse110.orgchart.entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import edu.ucsd.cse110.orgchart.database.EntryDAO;

public class EntryManager {
	
	private static OrgChartEntry[] getArrayFromList(List<?> entries) {
		if (entries == null) {
			return new OrgChartEntry[0];
		}

		OrgChartEntry[] result = new OrgChartEntry[entries.size()];

		int index = 0;
		for (Object obj : entries) {
			result[index++] = (OrgChartEntry) obj;
		}
		return result;
	}
	
	public static void addEntry(OrgChartEntry entry){
		EntryDAO.addEntry(entry);
	}
	
	public static OrgChartEntry getEntryById(long ID) {
		return EntryDAO.getEntry(ID);
	}

	public static OrgChartEntry[] getAllEntries() {
		List<?> list = EntryDAO.getAllEntries();
		return getArrayFromList(list);
	}
	
	public static Map<OrgChartEntry, List<OrgChartEntry>> getEntryTree() {
		OrgChartEntry[] entries;
		Map<Key, OrgChartEntry> entriesByKey;
		HashMap<OrgChartEntry, List<OrgChartEntry>> tree;

		entries = getAllEntries();

		if (entries == null)
			return null;

		entriesByKey = new HashMap<Key, OrgChartEntry>();
		tree = new HashMap<OrgChartEntry, List<OrgChartEntry>>();

		entriesByKey.put(null, null);

		for (OrgChartEntry entry : entries) {
			entriesByKey.put(entry.getKey(), entry);
		}

		tree.put(null, new ArrayList<OrgChartEntry>());
		for (OrgChartEntry entry : entries) {
			tree.put(entry, new ArrayList<OrgChartEntry>());
		}

		for (OrgChartEntry entry : entries) {
			tree.get(entriesByKey.get(entry.getParent())).add(entry);
		}

		Collections.sort(tree.get(null));
		for (OrgChartEntry entry : entries) {
			Collections.sort(tree.get(entry));
		}
		return tree;
	}
	
	/*
	 * ADD YOUR CODE HERE
	 */
	public static OrgChartEntry removeEntry(long entryID) {
		if(entryID==0)
			return null;
		if(getEntryById(entryID)==null)
			return null;
		long id;
		Map<OrgChartEntry, List<OrgChartEntry>> l=getEntryTree();
		List<OrgChartEntry> store= l.get(getEntryById(entryID));
		for(int i=0;i<store.size();i++)
		{
		id=store.get(i).getKey().getId();
		EntryManager.removeEntry(id);
		}
		return EntryDAO.removeEntry(entryID);
	}
}
