package stepper.dd.impl.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationData {

    private final List<String> columns;
    private final List<SingleRow> rows;
    private int numberOfRows;

    public RelationData(List<String> columns) {
        this.columns = columns;
        rows = new ArrayList<>();
        numberOfRows = 0;
    }

    public void addData(List<String> data) {
        SingleRow newRow = new SingleRow();
        for (int i = 0; i < columns.size(); i++) {
            newRow.addData(columns.get(i), data.get(i));
        }
        rows.add(newRow);
        numberOfRows++;
    }

    public List<String> getColumns(){
        return  columns;
    }

    public List<String> getRowDataByColumnsOrder(int rowId) {

        List<String> row = new ArrayList<>();
        for (String column : columns) {
            row.add(rows.get(rowId).getData(column));
        }
        return row;
    }
    public int size(){
        return numberOfRows;
    }

    private static class SingleRow {
        private final Map<String, String> data;

        public SingleRow() {
            data = new HashMap<>();
        }

        public void addData(String columnName, String value) {
            data.put(columnName, value);
        }
        public  String getData(String columnName){
            return data.get(columnName);
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("Names: ");
        for(String name : columns)
            ret.append(name).append(", ");
        ret.append("Number Of Rows: ").append(rows.size());
        return ret.toString();
    }
}
