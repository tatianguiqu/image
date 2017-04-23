package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dataService.WordService;

public class WordServiceImp implements WordService{

	@Override
	public void saveWordToSQL(HashMap<String, double[]> words) {
		// TODO Auto-generated method stub
		Statement stmt=DatabaseController.getStmt();
		Iterator<?> iter = words.entrySet().iterator();
		int count=0;
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			String key=(String) entry.getKey();
			String sql="insert into words values('"+key+"','";
			double[] val = (double[]) entry.getValue();
			for(int i=0;i<val.length;i++){
				sql=sql+val[i]+",";
			}
			sql=sql+"');";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
			if(count==1000){
				DatabaseController.commit();
				count=0;
			}
		}
		DatabaseController.commit();
	}

	@Override
	public HashMap<String, double[]> getWords() {
		// TODO Auto-generated method stub
		Statement stmt=DatabaseController.getNewStmt();
		HashMap<String, double[]> words=new HashMap<String, double[]>();
		String sql="select * from words";
		
		try {
			ResultSet rs=stmt.executeQuery(sql);
		
			while(rs.next()){
				String path=rs.getString(1);
				String[] str=rs.getString(2).split(",");
				double[] word=new double[str.length];
				for(int i=0;i<str.length;i++){
					word[i]=Double.valueOf(str[i]);
				}
				words.put(path, word);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return words;
	}

}
