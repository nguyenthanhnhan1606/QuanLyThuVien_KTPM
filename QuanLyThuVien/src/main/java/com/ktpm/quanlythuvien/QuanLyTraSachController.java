/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ktpm.quanlythuvien;

import com.ktpm.pojo.PhieuMuonSach;
import com.ktpm.pojo.User;
import com.ktpm.pojo.data2;
import static com.ktpm.quanlythuvien.UserMuonSachController.user;
import static com.ktpm.quanlythuvien.XacNhanSachController.pm;
import com.ktpm.services.PhieuMuonService;
import com.ktpm.services.SachService;
import com.ktpm.utils.MessageBox;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author THANH NHAN
 */
public class QuanLyTraSachController implements Initializable {
    
    private User us;
    public static PhieuMuonService pm = new PhieuMuonService();
    public static SachService s = new SachService();
    
    @FXML
    private TextField thoihan;
    @FXML
    private DatePicker ngaytra;
    @FXML
    TableView<PhieuMuonSach> tbPms;
    @FXML
    private TextField id;
    @FXML
    private TextField idDG;
    @FXML
    private TextField soluong;
    @FXML
    private TextField trangthai;
    @FXML
    private DatePicker ngaymuon;
    @FXML
    private DatePicker hantra;
    @FXML
    private TextField search;
    
    public void setUser(User u) {
        this.us = u;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTableColumns();
        try {
            this.ngaytra.setValue(LocalDate.now());
            loadTableData(null);
        } catch (SQLException ex) {
            Logger.getLogger(UserLichSuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.search.textProperty().addListener(e -> {
            try {
                this.loadTableData(this.search.getText());
            } catch (SQLException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }
    
    private void loadTableColumns() {
        TableColumn colID = new TableColumn("ID");
        colID.setCellValueFactory(new PropertyValueFactory("id"));
        
        TableColumn colMaDG = new TableColumn("Mã độc giả");
        colMaDG.setCellValueFactory(new PropertyValueFactory("id_user"));
        colMaDG.setPrefWidth(250);
        
        TableColumn colName = new TableColumn("Số lượng");
        colName.setCellValueFactory(new PropertyValueFactory("soluong"));
        colName.setPrefWidth(250);
        
        TableColumn colAuthor = new TableColumn("Ngày mượn");
        colAuthor.setCellValueFactory(new PropertyValueFactory("ngaymuon"));
        colAuthor.setPrefWidth(100);
        
        TableColumn colExport = new TableColumn("Hạn trả");
        colExport.setCellValueFactory(new PropertyValueFactory("hantra"));
        
        TableColumn colDescription = new TableColumn("Trạng thái");
        colDescription.setCellValueFactory(new PropertyValueFactory("trangthai"));
        colDescription.setPrefWidth(200);
        this.tbPms.getColumns().addAll(colID, colMaDG, colName, colAuthor, colExport, colDescription);
    }
    
    public void loadTableData(String kw) throws SQLException {
        List<PhieuMuonSach> pms = pm.getPhieuMuonSachTS(kw);
        this.tbPms.setItems(FXCollections.observableList(pms));
    }
    
    public void Load(MouseEvent evt) {
        PhieuMuonSach pms = tbPms.getSelectionModel().getSelectedItem();
        if (pms != null) {
            LocalDate date = Instant.ofEpochMilli(pms.getNgaymuon().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate date1 = Instant.ofEpochMilli(pms.getHantra().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            this.id.setText(String.valueOf(pms.getId()));
            this.idDG.setText(String.valueOf(pms.getId_user()));
            this.soluong.setText(String.valueOf(pms.getSoluong()));
            this.trangthai.setText(pms.getTrangthai());
            this.ngaymuon.setValue(date);
            this.hantra.setValue(date1);
            int i = date1.getDayOfYear();
            int j = this.ngaytra.getValue().getDayOfYear();
            int t = i - j;
            if (!this.trangthai.getText().equals("Đã trả")) {
                if (i - j > 0) {
                    this.thoihan.setText("Sớm " + t + " ngày");
                } else {
                    data2.setSongaytre(t*(-1));
                    this.thoihan.setText("Trễ " + t*(-1) + " ngày");
                }
            } else {
                this.thoihan.setText(this.trangthai.getText());
            }
            
        } else {
            MessageBox.getBox("Thông báo", "Bạn hãy chọn vào phiếu có sẵn!!!", Alert.AlertType.ERROR).show();
            
        }
        
    }
    
    public void chiTietTS(ActionEvent evt) throws IOException, SQLException {
        PhieuMuonSach pms = tbPms.getSelectionModel().getSelectedItem();
        if (pms != null) {
            data2.setIdts(pms.getId());
            data2.setTrangthaitra(pms.getTrangthai());
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChiTietTS.fxml"));
            Parent manageView = loader.load();
            Scene scene = new Scene(manageView);
            ChiTietTSController controller = loader.getController();
            controller.setUser(us);
            stage.setScene(scene);
            stage.show();
        } else {
            MessageBox.getBox("Thông báo", "Bạn chưa chọn phiếu cần xem!!!", Alert.AlertType.ERROR).show();
        }
        
    }
    
    public void thoat(ActionEvent evt) throws IOException, SQLException {
        User ur = user.getAD(this.us.getUsername(), this.us.getPassword());
        Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
        Parent manageView = loader.load();
        Scene scene = new Scene(manageView);
        AdminController controller = loader.getController();
        controller.setUser(ur);
        stage.setScene(scene);
        stage.show();
    }
    
}
