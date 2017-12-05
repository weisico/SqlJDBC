package com.weisico.sqljdbc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil  {

    private static Connection getSQLConnection(String ip, String user, String pwd, String db)
    {
        Connection con = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":1433/" + db + ";charset=gbk", user, pwd);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return con;
    }

    public static String QuerySQL()
    {
        String result = "";
        try
        {
            Connection conn = getSQLConnection("17.17.18.100", "sa", "sa", "bqdata");
            //String sql = "select top 10 * from bq_czy";
            String sql=" select top 10 a.被锁进程ID,a.被锁状态,a.被锁进程用户主机,c.ksmc 锁进程主机位置,a.被锁进程数据名称,a.被锁进程命令"+
                                               " ,b.锁进程ID,b.锁状态,b.锁进程用户主机,b.锁进程数据名称,b.锁进程命令"+
                    " from(select   spid 被锁进程ID,blocked 锁进程ID, status 被锁状态,"+
                    " SUBSTRING(hostname,1,12) 被锁进程用户主机, SUBSTRING(DB_NAME(dbid),1,10) 被锁进程数据名称, cmd 被锁进程命令"+
            " FROM master..sysprocesses"+
            " WHERE blocked>0 ) as a"+
            " left join (select isnull(spid,0) 锁进程ID,status 锁状态, cmd 锁进程命令,SUBSTRING(hostname,1,12) 锁进程用户主机, "+
                    " net_address MAC地址,SUBSTRING(DB_NAME(dbid),1,10) 锁进程数据名称"+
                    " from master..sysprocesses "+
                    " ) as b on a.锁进程ID=b.锁进程ID "+
                    " left join (select ksmc,replace([mac_address],'-','')mac_address  "+
                   " from openrowset('SQLOLEDB ', '17.17.18.200'; 'sa'; 'sa',[Blog_new].[dbo].[Mac_new])) as c  "+
                    " on b.MAC地址=c.mac_address";
            Statement stmt = conn.createStatement();//
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                String s1 = rs.getString("锁进程ID");
                String s2 = rs.getString("锁进程命令");
                result += s1 + "  -  " + s2 + "\n";
                System.out.println(s1 + "  -  " + s2);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
            result += "查询数据异常!" + e.getMessage();
        }
        return result;
    }

    public static void main(String[] args)
    {
        QuerySQL();
    }
}