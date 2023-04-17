/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ktpm.services;

import com.ktpm.pojo.PhieuMuonSach;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THANH NHAN
 */
public class PhieuMuonService {

    public boolean addPhieuMuon(PhieuMuonSach pm) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO phieumuonsach( ngaymuon,hantra, id_user, soluong, trangthai) VALUES(?, ?, ?,?,?)"; // SQL injection
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setDate(1, (Date) pm.getNgaymuon());
            stm.setDate(2, (Date) pm.getHantra());
            stm.setInt(3, pm.getId_user());
            stm.setInt(4, pm.getSoluong());
            stm.setString(5, pm.getTrangthai());
            stm.execute();
            try {
                conn.commit();
                return true;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return false;
            }
        }
    }

    public PhieuMuonSach getPM(int nam, int thang, int ngay, int id_user1) throws SQLException {
        PhieuMuonSach pms = null;
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM phieumuonsach where Year(ngaymuon)= ? && month(ngaymuon) = ? && day(ngaymuon)= ? && id_user= ? && trangthai=N'Chờ lấy sách'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, nam);
            stm.setInt(2, thang);
            stm.setInt(3, ngay);
            stm.setInt(4, id_user1);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pm = new PhieuMuonSach(rs.getInt("id"),
                        rs.getDate("ngaymuon"),
                        rs.getDate("hantra"),
                        rs.getInt("id_user"),
                        rs.getInt("soluong"),
                        rs.getString("trangthai"));
                pms = pm;
            }
        }
        return pms;
    }

    public PhieuMuonSach getPMTT(int nam, int thang, int ngay, int id_user1) throws SQLException {
        PhieuMuonSach pms = null;
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM phieumuonsach where Year(ngaymuon)= ? && month(ngaymuon) = ? && day(ngaymuon)= ? && id_user= ? && trangthai=N'Đang mượn sách'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, nam);
            stm.setInt(2, thang);
            stm.setInt(3, ngay);
            stm.setInt(4, id_user1);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pm = new PhieuMuonSach(rs.getInt("id"),
                        rs.getDate("ngaymuon"),
                        rs.getDate("hantra"),
                        rs.getInt("id_user"),
                        rs.getInt("soluong"),
                        rs.getString("trangthai"));
                pms = pm;
            }
        }
        return pms;
    }

    public boolean kiemTraMuon(int id) throws SQLException {
        PhieuMuonSach pms = null;
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM phieumuonsach where id_user= ? && trangthai=N'Chờ lấy sách' || id_user= ? && trangthai=N'Đang mượn sách'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, id);
            stm.setInt(2, id);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pm = new PhieuMuonSach(rs.getInt("id"),
                        rs.getDate("ngaymuon"),
                        rs.getDate("hantra"),
                        rs.getInt("id_user"),
                        rs.getInt("soluong"),
                        rs.getString("trangthai"));
                pms = pm;
            }
        }
        if (pms == null) {
            return true;
        }
        return false;

    }

    public List<PhieuMuonSach> getHis(int id) throws SQLException {
        List<PhieuMuonSach> pm = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "select * from phieumuonsach p where p.id_user=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pms = new PhieuMuonSach(rs.getInt("id"), rs.getDate("ngaymuon"), rs.getDate("hantra"), rs.getInt("id_user"), rs.getInt("soluong"), rs.getString("trangthai"));
                pm.add(pms);
            }
        }
        return pm;
    }

    public List<PhieuMuonSach> getPhieuMuonSach() throws SQLException {
        List<PhieuMuonSach> pm = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "select * from phieumuonsach";
            PreparedStatement stm = conn.prepareCall(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pms = new PhieuMuonSach(rs.getInt("id"), rs.getDate("ngaymuon"), rs.getDate("hantra"), rs.getInt("id_user"), rs.getInt("soluong"), rs.getString("trangthai"));
                pm.add(pms);
            }
        }
        return pm;
    }

    public List<PhieuMuonSach> getPhieuMuonSachXN(String kw) throws SQLException {
        List<PhieuMuonSach> pm = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "select * from phieumuonsach where (trangthai=N'Chờ lấy sách' || trangthai=N'Đã hủy')";
            if (kw != null && !kw.isEmpty()) {
                sql += " && id_user=?";
            }
            PreparedStatement stm = conn.prepareCall(sql);
            if (kw != null && !kw.isEmpty()) {
                stm.setInt(1, Integer.parseInt(kw));
            }
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pms = new PhieuMuonSach(rs.getInt("id"), rs.getDate("ngaymuon"), rs.getDate("hantra"), rs.getInt("id_user"), rs.getInt("soluong"), rs.getString("trangthai"));
                pm.add(pms);
            }
        }
        return pm;
    }

    public List<PhieuMuonSach> getPhieuMuonSachTS(String kw) throws SQLException {
        List<PhieuMuonSach> pm = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "select * from phieumuonsach where (trangthai=N'Đang mượn sách' || trangthai=N'Đã trả')";
            if (kw != null && !kw.isEmpty()) {
                sql += " && id_user=?";
            }
            PreparedStatement stm = conn.prepareCall(sql);
            if (kw != null && !kw.isEmpty()) {
                stm.setInt(1, Integer.parseInt(kw));
            }
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pms = new PhieuMuonSach(rs.getInt("id"), rs.getDate("ngaymuon"), rs.getDate("hantra"), rs.getInt("id_user"), rs.getInt("soluong"), rs.getString("trangthai"));
                pm.add(pms);
            }
        }
        return pm;
    }

    public boolean updateTrangThaiPM(int id) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "Update phieumuonsach set trangthai=N'Đã hủy' Where id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            int r = stm.executeUpdate();
            return r > 0;
        }
    }

    public boolean updateTrangThaiPMSS(int id) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "Update phieumuonsach set trangthai=N'Đang mượn sách' Where id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            int r = stm.executeUpdate();
            return r > 0;
        }
    }

    public boolean updateTrangThaiPMDT(int id) throws SQLException {
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "Update phieumuonsach set trangthai=N'Đã trả' Where id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            int r = stm.executeUpdate();
            return r > 0;
        }
    }

    public List<PhieuMuonSach> getPhieuMuonSachQ1(int nam) throws SQLException {
        List<PhieuMuonSach> pm = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM phieumuonsach where quarter(ngaymuon)=1 && (trangthai=N'Đã trả' || trangthai=N'Đang mượn sách') && Year(ngaymuon)=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, nam);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pms = new PhieuMuonSach(rs.getInt("id"), rs.getDate("ngaymuon"), rs.getDate("hantra"), rs.getInt("id_user"), rs.getInt("soluong"), rs.getString("trangthai"));
                pm.add(pms);
            }
        }
        return pm;
    }

    public List<PhieuMuonSach> getPhieuMuonSachQ2(int nam) throws SQLException {
        List<PhieuMuonSach> pm = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM phieumuonsach where quarter(ngaymuon)=2 && (trangthai=N'Đã trả' || trangthai=N'Đang mượn sách') && year(ngaymuon)=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, nam);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pms = new PhieuMuonSach(rs.getInt("id"), rs.getDate("ngaymuon"), rs.getDate("hantra"), rs.getInt("id_user"), rs.getInt("soluong"), rs.getString("trangthai"));
                pm.add(pms);
            }
        }
        return pm;
    }

    public List<PhieuMuonSach> getPhieuMuonSachQ3(int nam) throws SQLException {
        List<PhieuMuonSach> pm = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM phieumuonsach where quarter(ngaymuon)=3 && (trangthai=N'Đã trả' || trangthai=N'Đang mượn sách') && year(ngaymuon)=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, nam);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pms = new PhieuMuonSach(rs.getInt("id"), rs.getDate("ngaymuon"), rs.getDate("hantra"), rs.getInt("id_user"), rs.getInt("soluong"), rs.getString("trangthai"));
                pm.add(pms);
            }
        }
        return pm;
    }

    public List<PhieuMuonSach> getPhieuMuonSachQ4(int nam) throws SQLException {
        List<PhieuMuonSach> pm = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM phieumuonsach where quarter(ngaymuon)=4 && (trangthai=N'Đã trả' || trangthai=N'Đang mượn sách') && year(ngaymuon)=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, nam);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pms = new PhieuMuonSach(rs.getInt("id"), rs.getDate("ngaymuon"), rs.getDate("hantra"), rs.getInt("id_user"), rs.getInt("soluong"), rs.getString("trangthai"));
                pm.add(pms);
            }
        }
        return pm;
    }
    
    ///đang test đễ fix
    public List<PhieuMuonSach> get1PhieuMuon(int idpm) throws SQLException {
        List<PhieuMuonSach> pm = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConn()) {
            String sql = "SELECT * FROM phieumuonsach where id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, idpm);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                PhieuMuonSach pms = new PhieuMuonSach(rs.getInt("id"), rs.getDate("ngaymuon"), rs.getDate("hantra"), rs.getInt("id_user"), rs.getInt("soluong"), rs.getString("trangthai"));
                pm.add(pms);
            }
        }
        return pm;
    }
    
    
}
