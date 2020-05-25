import json
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtCore import QFile

Custom_set={}
Dfault_set={}
Dfault_set['Settings']={
    'nameserver': 'Server',
    'ip': '127.0.0.1',
    'port': 1234
}

class Ui_Dialog(object):
    def setupUi(self, Dialog):
        Dialog.setObjectName("Dialog")
        Dialog.resize(515, 340)
        Dialog.setMinimumSize(QtCore.QSize(515, 340))
        Dialog.setMaximumSize(QtCore.QSize(515, 340))
        self.gridLayout_2 = QtWidgets.QGridLayout(Dialog)
        self.gridLayout_2.setObjectName("gridLayout_2")
        self.SingIn = QtWidgets.QGroupBox(Dialog)
        self.SingIn.setMinimumSize(QtCore.QSize(321, 281))
        self.SingIn.setMaximumSize(QtCore.QSize(321, 281))
        self.SingIn.setObjectName("SingIn")
        self.gridLayout = QtWidgets.QGridLayout(self.SingIn)
        self.gridLayout.setContentsMargins(20, -1, -1, 20)
        self.gridLayout.setObjectName("gridLayout")
        self.User_layout = QtWidgets.QHBoxLayout()
        self.User_layout.setContentsMargins(5, -1, -1, -1)
        self.User_layout.setSpacing(7)
        self.User_layout.setObjectName("User_layout")
        self.label_Username = QtWidgets.QLabel(self.SingIn)
        self.label_Username.setObjectName("label_Username")
        self.User_layout.addWidget(self.label_Username)
        self.lineEdit_Username = QtWidgets.QLineEdit(self.SingIn)
        self.lineEdit_Username.setObjectName("lineEdit_Username")
        self.User_layout.addWidget(self.lineEdit_Username)
        self.gridLayout.addLayout(self.User_layout, 2, 0, 1, 1)
        self.Pass_layout = QtWidgets.QHBoxLayout()
        self.Pass_layout.setContentsMargins(5, -1, -1, -1)
        self.Pass_layout.setSpacing(10)
        self.Pass_layout.setObjectName("Pass_layout")
        self.label_Password = QtWidgets.QLabel(self.SingIn)
        self.label_Password.setMinimumSize(QtCore.QSize(63, 0))
        self.label_Password.setObjectName("label_Password")
        self.Pass_layout.addWidget(self.label_Password)
        self.lineEdit_Password = QtWidgets.QLineEdit(self.SingIn)
        self.lineEdit_Password.setObjectName("lineEdit_Password")
        self.Pass_layout.addWidget(self.lineEdit_Password)
        self.label = QtWidgets.QLabel(self.SingIn)
        self.label.setObjectName("label")
        self.Pass_layout.addWidget(self.label)
        self.lineEdit = QtWidgets.QLineEdit(self.SingIn)
        self.lineEdit.setMinimumSize(QtCore.QSize(50, 20))
        self.lineEdit.setMaximumSize(QtCore.QSize(50, 20))
        self.lineEdit.setObjectName("lineEdit")
        self.Pass_layout.addWidget(self.lineEdit)
        self.gridLayout.addLayout(self.Pass_layout, 3, 0, 1, 1)
        spacerItem = QtWidgets.QSpacerItem(20, 40, QtWidgets.QSizePolicy.Minimum, QtWidgets.QSizePolicy.Expanding)
        self.gridLayout.addItem(spacerItem, 4, 0, 1, 1)
        spacerItem1 = QtWidgets.QSpacerItem(20, 40, QtWidgets.QSizePolicy.Minimum, QtWidgets.QSizePolicy.Expanding)
        self.gridLayout.addItem(spacerItem1, 1, 0, 1, 2)
        self.label_text_info = QtWidgets.QLabel(self.SingIn)
        self.label_text_info.setObjectName("label_text_info")
        self.gridLayout.addWidget(self.label_text_info, 0, 0, 1, 1)
        self.gridLayout_2.addWidget(self.SingIn, 0, 4, 3, 1)
        self.buttonBox = QtWidgets.QDialogButtonBox(Dialog)
        self.buttonBox.setOrientation(QtCore.Qt.Horizontal)
        self.buttonBox.setStandardButtons(QtWidgets.QDialogButtonBox.Cancel|QtWidgets.QDialogButtonBox.Ok)
        self.buttonBox.setObjectName("buttonBox")
        self.gridLayout_2.addWidget(self.buttonBox, 3, 4, 1, 1)
        self.Img = QtWidgets.QLabel(Dialog)
        self.Img.setMinimumSize(QtCore.QSize(150, 150))
        self.Img.setMaximumSize(QtCore.QSize(150, 150))
        self.Img.setText("")
        self.Img.setPixmap(QtGui.QPixmap("Icon/Option_sever.png"))
        self.Img.setScaledContents(True)
        self.Img.setObjectName("Img")
        self.gridLayout_2.addWidget(self.Img, 1, 2, 1, 1)
        spacerItem2 = QtWidgets.QSpacerItem(172, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
        self.gridLayout_2.addItem(spacerItem2, 1, 3, 1, 1)
        

        self.retranslateUi(Dialog)
        self.buttonBox.accepted.connect(self.write_conf)
        self.buttonBox.accepted.connect(Dialog.accept)
        self.buttonBox.rejected.connect(Dialog.reject)
        QtCore.QMetaObject.connectSlotsByName(Dialog)
        self.Read_conf()

    def retranslateUi(self, Dialog):
        _translate = QtCore.QCoreApplication.translate
        Dialog.setWindowTitle(_translate("Dialog", "Options Server"))
        self.SingIn.setTitle(_translate("Dialog", "Sever Config"))
        self.label_Username.setText(_translate("Dialog", "Server Name:"))
        self.label_Password.setText(_translate("Dialog", "IP Server:"))
        self.label.setText(_translate("Dialog", "Port:"))
        self.label_text_info.setText(_translate("Dialog", "Insert Name Server and IP Server"))
    
    def Read_conf(self):
        if QFile("settings.json").exists() == True:
            file_conf = open("settings.json", "r", encoding="utf-8")
            Custom_set=json.load(file_conf)
            file_conf.close()
            self.lineEdit_Username.setText(Custom_set['Settings']['nameserver'])
            self.lineEdit_Password.setText(Custom_set['Settings']['ip'])
            self.lineEdit.setText(str(Custom_set['Settings']['port']))
        else:
            self.create_conf()

    def create_conf(self):
        file_conf = open("settings.json", "w+", encoding="utf-8")
        json.dump(Dfault_set, file_conf)
        file_conf.close()
        self.Read_conf()
    
    def write_conf(self):
        name=self.lineEdit_Username.text()
        ip=self.lineEdit_Password.text()
        port=int(self.lineEdit.text())
        Custom_set['Settings']={
            'nameserver': name,
            'ip': ip,
            'port': port
        }
        file_conf = open("settings.json", "w+", encoding="utf-8")
        json.dump(Custom_set, file_conf)
        file_conf.close()


if __name__ == "__main__":
    import sys
    app = QtWidgets.QApplication(sys.argv)
    Dialog = QtWidgets.QDialog()
    ui = Ui_Dialog()
    ui.setupUi(Dialog)
    Dialog.show()
    sys.exit(app.exec_())
