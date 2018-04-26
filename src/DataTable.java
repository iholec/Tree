import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class DataTable<KC, KR, V> extends LinkedList<LinkedList<V>> {

	private LinkedHashSet<KC> columnLabels = new LinkedHashSet<>();
	private LinkedHashSet<KR> rowLabels = new LinkedHashSet<>();

	public List<V> getColumn(KC columnKey) {
		return getColumn(new LinkedList<>(columnLabels).indexOf(columnKey));
	}

	public List<V> getColumn(int index) {
		ArrayList<V> rv = new ArrayList<>();
		for (LinkedList<V> row : this) {
			rv.add(row.get(index));
		}
		return rv;
	}

	public List<V> getRow(KR rowKey) {
		return getColumn(new LinkedList<>(rowLabels).indexOf(rowKey));
	}

	public List<V> getRow(int index) {
		return this.get(index);
	}

	public ArrayList<KC> getColumnLabels() {
		return new ArrayList<>(columnLabels);
	}

	public ArrayList<KR> getRowLabels() {
		return new ArrayList<>(rowLabels);
	}

	public void setElement(int columnIndex, int rowIndex, V value) {
		if((rowLabels.size() <= rowIndex) || (columnLabels.size() <= columnIndex)) {
			throw new IndexOutOfBoundsException();
		} else {
			if(this.get(rowIndex).get(columnIndex) != null)  {
				this.get(rowIndex).remove(columnIndex); 
			}
				this.get(rowIndex).add(columnIndex, value);
			
		}
	}
	
	public void addRow(KR rowKey, List<V> row) {
		rowLabels.add(rowKey);
		this.add((LinkedList<V>) row);
	}
	
	public void addColumn(KC columnKey, List<V> column) {
		columnLabels.add(columnKey);
		
		while(column.size() > this.size()) {
			this.add(new LinkedList<>());
		}
		
		for (int i = 0; i < column.size(); i++) {
			this.get(i).add(column.get(i));
		}
	}
	
	/**
	 * Calculates the next index for a new column
	 * @return
	 */
	private int clacNextColumnIndex() {
		int index = 0;
		for (LinkedList<V> row : this) {
			if(index < row.size()) {
				index = row.size();
			}
		}
		return index;	
	}

	@Override
	public String toString() {
		String str = "";
		for (LinkedList<V> row : this) {
			for (V element : row) {
				if(element != null) {
					str += "\t" + element.toString();
				} else {
					str += "\t";
				}
			}
			str += "\n";
		}
		return str;
	}

}
