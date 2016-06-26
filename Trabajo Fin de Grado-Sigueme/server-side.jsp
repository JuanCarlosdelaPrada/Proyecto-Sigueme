/*
 * Script:    DataTables server-side script for Java Sever Pages
 * Copyright: 2010 - Wang Chao
 * License:   GPL v2 or BSD (3-point)
 */
 
<%@page import="java.util.*"%>
<%@page import="com.chest.web.db.ConnectManager"%>
<%@page import="java.sql.*"%>
<%@page import="org.json.*"%>
<%
    String[] cols = { "engine", "browser", "platform", "version", "grade" };
    String table = "ajax";
     
    JSONObject result = new JSONObject();
    JSONArray array = new JSONArray();
    int amount = 10;
    int start = 0;
    int echo = 0;
    int col = 0;
     
    String engine = "";
    String browser = "";
    String platform = "";
    String version = "";
    String grade = "";
 
    String dir = "asc";
    String sStart = request.getParameter("iDisplayStart");
    String sAmount = request.getParameter("iDisplayLength");
    String sEcho = request.getParameter("sEcho");
    String sCol = request.getParameter("iSortCol_0");
    String sdir = request.getParameter("sSortDir_0");
     
    engine = request.getParameter("sSearch_0");
    browser = request.getParameter("sSearch_1");
    platform = request.getParameter("sSearch_2");
    version = request.getParameter("sSearch_3");
    grade = request.getParameter("sSearch_4");
     
    List<String> sArray = new ArrayList<String>();
    if (!engine.equals("")) {
        String sEngine = " engine like '%" + engine + "%'";
        sArray.add(sEngine);
        //or combine the above two steps as:
        //sArray.add(" engine like '%" + engine + "%'");
        //the same as followings
    }
    if (!browser.equals("")) {
        String sBrowser = " browser like '%" + browser + "%'";
        sArray.add(sBrowser);
    }
    if (!platform.equals("")) {
        String sPlatform = " platform like '%" + platform + "%'";
        sArray.add(sPlatform);
    }
    if (!version.equals("")) {
        String sVersion = " version like '%" + version + "%'";
        sArray.add(sVersion);
    }
    if (!grade.equals("")) {
        String sGrade = " grade like '%" + grade + "%'";
        sArray.add(sGrade);
    }
     
    String individualSearch = "";
    if(sArray.size()==1){
        individualSearch = sArray.get(0);
    }else if(sArray.size()>1){
        for(int i=0;i<sArray.size()-1;i++){
            individualSearch += sArray.get(i)+ " and ";
        }
        individualSearch += sArray.get(sArray.size()-1);
    }
     
    if (sStart != null) {
        start = Integer.parseInt(sStart);
        if (start < 0)
            start = 0;
    }
    if (sAmount != null) {
        amount = Integer.parseInt(sAmount);
        if (amount < 10 || amount > 100)
            amount = 10;
    }
    if (sEcho != null) {
        echo = Integer.parseInt(sEcho);
    }
    if (sCol != null) {
        col = Integer.parseInt(sCol);
        if (col < 0 || col > 5)
            col = 0;
    }
    if (sdir != null) {
        if (!sdir.equals("asc"))
            dir = "desc";
    }
    String colName = cols[col];
    int total = 0;
    Connection conn = ConnectManager.getConnection();
    try {
        String sql = "SELECT count(*) FROM "+table;
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            total = rs.getInt("count(*)");
        }
    }catch(Exception e){
         
    }
    int totalAfterFilter = total;
    //result.put("sEcho",echo);
 
    try {
        String searchSQL = "";
        String sql = "SELECT * FROM "+table;
        String searchTerm = request.getParameter("sSearch");
        String globeSearch =  " where (engine like '%"+searchTerm+"%'"
                                + " or browser like '%"+searchTerm+"%'"
                                + " or platform like '%"+searchTerm+"%'"
                                + " or version like '%"+searchTerm+"%'"
                                + " or grade like '%"+searchTerm+"%')";
        if(searchTerm!=""&&individualSearch!=""){
            searchSQL = globeSearch + " and " + individualSearch;
        }
        else if(individualSearch!=""){
            searchSQL = " where " + individualSearch;
        }else if(searchTerm!=""){
            searchSQL=globeSearch;
        }
        sql += searchSQL;
        sql += " order by " + colName + " " + dir;
        sql += " limit " + start + ", " + amount;
 
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            JSONArray ja = new JSONArray();
            ja.put(rs.getString("engine"));
            ja.put(rs.getString("browser"));
            ja.put(rs.getString("platform"));
            ja.put(rs.getString("version"));
            ja.put(rs.getString("grade"));
            array.put(ja);
        }
        String sql2 = "SELECT count(*) FROM "+table;
        if (searchTerm != "") {
            sql2 += searchSQL;
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                totalAfterFilter = rs2.getInt("count(*)");
            }
        }
        result.put("iTotalRecords", total);
        result.put("iTotalDisplayRecords", totalAfterFilter);
        result.put("aaData", array);
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        out.print(result);
        conn.close();
    } catch (Exception e) {
 
    }
%>