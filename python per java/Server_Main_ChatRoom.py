import os
import sys
import threading
import time
import socket
import select
import errno
import queue
import json
from PyQt5.QtWidgets import QApplication, QMainWindow, QDialog
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtCore import QFile
from logzero import logger as log
from Options_Server import Ui_Dialog
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from Crypto import Random
# from cryptography.hazmat.primitives import hashes
# from cryptography.hazmat.primitives.asymmetric import padding


HEADER_LENGTH = 10

clients_list_show={}
clients={}
sockets_list=[]
Custom_set={}
Dfault_set={}
Dfault_set['Settings']={
    'nameserver': 'Server',
    'ip': '127.0.0.1',
    'port': 1234
}
thread_Engine=""
id_list = 0
Run = False
show_print = False
PriKey = ""
PubKey = ""

class Dialog_S(QDialog, Ui_Dialog):
    def __init__(self, parent=None):
        QDialog.__init__(self, parent)
        self.setupUi(self)

class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(729, 498)
        MainWindow.setMinimumSize(QtCore.QSize(729, 498))
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.gridLayout = QtWidgets.QGridLayout(self.centralwidget)
        self.gridLayout.setObjectName("gridLayout")
        self.textEditor = QtWidgets.QTextEdit(self.centralwidget)
        self.textEditor.setMinimumSize(QtCore.QSize(0, 0))
        palette = QtGui.QPalette()
        brush = QtGui.QBrush(QtGui.QColor(248, 248, 242))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Text, brush)
        brush = QtGui.QBrush(QtGui.QColor(39, 40, 34))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Base, brush)
        brush = QtGui.QBrush(QtGui.QColor(248, 248, 242, 128))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.PlaceholderText, brush)
        brush = QtGui.QBrush(QtGui.QColor(248, 248, 242))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Text, brush)
        brush = QtGui.QBrush(QtGui.QColor(39, 40, 34))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Base, brush)
        brush = QtGui.QBrush(QtGui.QColor(248, 248, 242, 128))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.PlaceholderText, brush)
        brush = QtGui.QBrush(QtGui.QColor(120, 120, 120))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Text, brush)
        brush = QtGui.QBrush(QtGui.QColor(240, 240, 240))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Base, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0, 128))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.PlaceholderText, brush)
        self.textEditor.setPalette(palette)
        self.textEditor.setAutoFillBackground(False)
        self.textEditor.setUndoRedoEnabled(False)
        self.textEditor.setReadOnly(True)
        self.textEditor.setObjectName("textEditor")
        self.gridLayout.addWidget(self.textEditor, 0, 0, 1, 1)
        self.horizontalLayout_2 = QtWidgets.QHBoxLayout()
        self.horizontalLayout_2.setContentsMargins(-1, -1, 0, 0)
        self.horizontalLayout_2.setObjectName("horizontalLayout_2")
        self.lineEdit = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEdit.setObjectName("lineEdit")
        self.horizontalLayout_2.addWidget(self.lineEdit)
        self.pushButton = QtWidgets.QPushButton(self.centralwidget)
        self.pushButton.setObjectName("pushButton")
        self.horizontalLayout_2.addWidget(self.pushButton)
        self.gridLayout.addLayout(self.horizontalLayout_2, 1, 0, 1, 1)
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QtWidgets.QMenuBar(MainWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 729, 21))
        self.menubar.setObjectName("menubar")
        self.menuFile = QtWidgets.QMenu(self.menubar)
        self.menuFile.setObjectName("menuFile")
        self.menuInfo = QtWidgets.QMenu(self.menubar)
        self.menuInfo.setObjectName("menuInfo")
        MainWindow.setMenuBar(self.menubar)
        self.toolBar = QtWidgets.QToolBar(MainWindow)
        self.toolBar.setMovable(False)
        self.toolBar.setObjectName("toolBar")
        MainWindow.addToolBar(QtCore.Qt.TopToolBarArea, self.toolBar)
        self.statusbar = QtWidgets.QStatusBar(MainWindow)
        self.statusbar.setObjectName("statusbar")
        MainWindow.setStatusBar(self.statusbar)
        self.actionStart = QtWidgets.QAction(MainWindow)
        icon = QtGui.QIcon()
        icon.addPixmap(QtGui.QPixmap("Icon/Start_sever.png"), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionStart.setIcon(icon)
        self.actionStart.setObjectName("actionStart")
        self.actionStop = QtWidgets.QAction(MainWindow)
        self.actionStop.setEnabled(False)
        icon1 = QtGui.QIcon()
        icon1.addPixmap(QtGui.QPixmap("Icon/Stop_sever.png"), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionStop.setIcon(icon1)
        self.actionStop.setObjectName("actionStop")
        self.actionExit = QtWidgets.QAction(MainWindow)
        icon2 = QtGui.QIcon()
        icon2.addPixmap(QtGui.QPixmap("Icon/shell32_240.ico"), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionExit.setIcon(icon2)
        self.actionExit.setObjectName("actionExit")
        self.actionAbout_Qt5 = QtWidgets.QAction(MainWindow)
        icon3 = QtGui.QIcon()
        icon3.addPixmap(QtGui.QPixmap("Icon/qt_94938.ico"), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionAbout_Qt5.setIcon(icon3)
        self.actionAbout_Qt5.setObjectName("actionAbout_Qt5")
        self.actionAbout_Server_ChatRoom = QtWidgets.QAction(MainWindow)
        self.actionAbout_Server_ChatRoom.setObjectName("actionAbout_Server_ChatRoom")
        self.actionOptions = QtWidgets.QAction(MainWindow)
        icon4 = QtGui.QIcon()
        icon4.addPixmap(QtGui.QPixmap("Icon/Option_sever.png"), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionOptions.setIcon(icon4)
        self.actionOptions.setObjectName("actionOptions")
        self.actionHelp = QtWidgets.QAction(MainWindow)
        icon5 = QtGui.QIcon()
        icon5.addPixmap(QtGui.QPixmap("Icon/imageres_99.ico"), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionHelp.setIcon(icon5)
        self.actionHelp.setObjectName("actionHelp")
        self.actionClear_CLI = QtWidgets.QAction(MainWindow)
        icon6 = QtGui.QIcon()
        icon6.addPixmap(QtGui.QPixmap("Icon/iconfinder_terminal_clear.png"), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionClear_CLI.setIcon(icon6)
        self.actionClear_CLI.setObjectName("actionClear_CLI")
        self.menuFile.addAction(self.actionStart)
        self.menuFile.addAction(self.actionStop)
        self.menuFile.addSeparator()
        self.menuFile.addAction(self.actionOptions)
        self.menuFile.addSeparator()
        self.menuFile.addAction(self.actionExit)
        self.menuInfo.addAction(self.actionHelp)
        self.menuInfo.addSeparator()
        self.menuInfo.addAction(self.actionAbout_Qt5)
        self.menuInfo.addAction(self.actionAbout_Server_ChatRoom)
        self.menubar.addAction(self.menuFile.menuAction())
        self.menubar.addAction(self.menuInfo.menuAction())
        self.toolBar.addAction(self.actionStart)
        self.toolBar.addAction(self.actionStop)
        self.toolBar.addSeparator()
        self.toolBar.addAction(self.actionClear_CLI)
        self.toolBar.addSeparator()
        self.toolBar.addAction(self.actionOptions)

        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)
        
        self.pushButton.clicked.connect(self.check_com)
        self.actionStart.triggered.connect(self.Start_Engine)
        self.actionStop.triggered.connect(self.Stop_sever)
        self.actionOptions.triggered.connect(self.Dialog_set)
        self.actionExit.triggered.connect(self.quit)

        self.Read_conf()

    def retranslateUi(self, MainWindow):
        _translate = QtCore.QCoreApplication.translate
        MainWindow.setWindowTitle(_translate("MainWindow", "Server Control ChatRoom"))
        self.pushButton.setText(_translate("MainWindow", "Submit"))
        self.pushButton.setShortcut(_translate("MainWindow", "Return"))
        self.menuFile.setTitle(_translate("MainWindow", "File"))
        self.menuInfo.setTitle(_translate("MainWindow", "Info"))
        self.toolBar.setWindowTitle(_translate("MainWindow", "toolBar"))
        self.actionStart.setText(_translate("MainWindow", "Start"))
        self.actionStart.setShortcut(_translate("MainWindow", "Ctrl+S"))
        self.actionStop.setText(_translate("MainWindow", "Stop"))
        self.actionStop.setShortcut(_translate("MainWindow", "Ctrl+D"))
        self.actionExit.setText(_translate("MainWindow", "Exit"))
        self.actionExit.setShortcut(_translate("MainWindow", "Ctrl+Q"))
        self.actionAbout_Qt5.setText(_translate("MainWindow", "About Qt5"))
        self.actionAbout_Server_ChatRoom.setText(_translate("MainWindow", "About Server ChatRoom"))
        self.actionAbout_Server_ChatRoom.setShortcut(_translate("MainWindow", "Ctrl+I"))
        self.actionOptions.setText(_translate("MainWindow", "Options"))
        self.actionOptions.setShortcut(_translate("MainWindow", "Ctrl+P"))
        self.actionHelp.setText(_translate("MainWindow", "Help"))
        self.actionHelp.setShortcut(_translate("MainWindow", "Ctrl+H"))
        self.actionClear_CLI.setText(_translate("MainWindow", "Clear CLI"))
        self.actionClear_CLI.setShortcut(_translate("MainWindow", "Ctrl+L"))

    # Config
    def Read_conf(self):
        global Custom_set
        if QFile("settings.json").exists() == True:
            file_conf = open("settings.json", "r", encoding="utf-8")
            Custom_set=json.load(file_conf)
            file_conf.close()
        else:
            self.create_conf()
            self.Read_conf()
    
    def create_conf(self):
        file_conf = open("settings.json", "w+", encoding="utf-8")
        json.dump(Dfault_set, file_conf)
        file_conf.close()

    # Dialog
    def Dialog_set(self):
        Dialog = Dialog_S()
        Dialog.show()
        rsp = Dialog.exec_()

    # RSA Key
    def Generate_Key(self):
        global PriKey, PubKey
        random_generator = Random.new().read
        PriKey = RSA.generate(1024, random_generator)

        PubKey = PriKey.publickey()
        # print(f"Public key:  (n={hex(pubKey.n)}, e={hex(pubKey.e)})")
        PubKey = PubKey.exportKey(format='PEM',pkcs=8)
        # print(pubKeyPEM.decode('ascii'))

        # print(f"Private key: (n={hex(pubKey.n)}, d={hex(keyPair.d)})")
        privKeyPEM = PriKey.exportKey()
        # print(privKeyPEM.decode('ascii'))

        

    # Engines
    def Start_Engine(self):
        global thread_Engine
        thread_Engine = threading.Thread(target=(self.Start_sever), args=())
        thread_Engine.start()
        if thread_Engine == 0:
           thread_Engine.join()

    # Stop Server
    def Stop_sever(self):
        global Run
        Run = False

    # Start Server
    def Start_sever(self):
        global Custom_set, clients_list_show, id_list, clients, Run, PubKey, PriKey ,sockets_list, show_print
        server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        server_socket.bind((Custom_set['Settings']['ip'], Custom_set['Settings']['port']))
        server_socket.listen()
        sockets_list = [server_socket]
        clients = {}
        clients_list_show.clear()
        Run = True
        self.Generate_Key()
        log.info('Listening for connections on {}:{}...'.format(Custom_set['Settings']['ip'],Custom_set['Settings']['port']))
        self.textEditor.append('[ INFO ] Listening for connections on {}:{}...'.format(Custom_set['Settings']['ip'],Custom_set['Settings']['port']))
        self.textEditor.moveCursor(QtGui.QTextCursor.End)
        self.actionStart.setEnabled(False)
        self.actionStop.setEnabled(True)
        
        while True:
            if Run==False:
                break
            read_sockets, _, exception_sockets = select.select(sockets_list, [], sockets_list)
            for notified_socket in read_sockets:
                if notified_socket == server_socket:                   
                    client_socket, client_address = server_socket.accept()                   
                    user = self.receive_message(client_socket)                   
                    if user is False:
                        continue                    
                    sockets_list.append(client_socket)                    
                    clients[client_socket] = user                   
                    message = PubKey
                    message_header = f"{len(message):<{HEADER_LENGTH}}".encode('utf-8')
                    client_socket.send(user['header'] + user['data'] + message_header + message)

                    message_header = client_socket.recv(HEADER_LENGTH)
                    if not len(message_header):
                        print('Connection closed by the Client')
                    message_length = int(message_header.decode('utf-8').strip())
                    message = client_socket.recv(message_length)
                    PubKey_cliet = message


                    log.info('Accepted new connection from {}:{}, username: {}'.format(*client_address, user['data'].decode('utf-8')))
                    self.textEditor.append('[ INFO ] Accepted new connection from {}:{}, username: {}'.format(*client_address, user['data'].decode('utf-8')))
                    self.textEditor.moveCursor(QtGui.QTextCursor.End)
                    clients_list_show[id_list]={'sock':client_socket, 'addres':client_address, 'PubKey':PubKey_cliet, 'data':user['data'].decode('utf-8')}
                    id_list=id_list+1
                else:
                    message = self.receive_message(notified_socket)
                    if message is False:
                        log.info('Closed connection from: {}'.format(clients[notified_socket]['data'].decode('utf-8')))
                        self.textEditor.append('[ INFO ] Closed connection from: {}'.format(clients[notified_socket]['data'].decode('utf-8')))
                        self.textEditor.moveCursor(QtGui.QTextCursor.End)
                        kry_copy = tuple(clients_list_show.keys())
                        for k in kry_copy:
                            if clients_list_show[k]['sock'] == notified_socket:
                                del clients_list_show[k]

                        sockets_list.remove(notified_socket)
                        del clients[notified_socket]
                        continue

                    msg = message['data']
                    # decryptor = PKCS1_OAEP.new(PriKey)
                    # decrypted = decryptor.decrypt(msg)

                    user = clients[notified_socket]

                    # log.debug(f'Received message from {user["data"].decode("utf-8")}: {decrypted.decode("utf-8")}')
                    # self.textEditor.append(f'[ DEBUG ] Received message from {user["data"].decode("utf-8")}: {decrypted.decode("utf-8")}')
                    if show_print == True:
                        self.textEditor.append(f'[ CHAT ] Message from {user["data"].decode("utf-8")}: {msg.decode("utf-8")}')
                        self.textEditor.moveCursor(QtGui.QTextCursor.End)
                    for client_socket in clients:
                        if client_socket != notified_socket:
                            for y in clients_list_show:
                                if clients_list_show[y]['sock'] == client_socket:
                                    PubKey_ex = RSA.import_key(clients_list_show[y].get('PubKey'))
                                    # encryptor = PKCS1_OAEP.new(PubKey_ex)
                                    # encrypted = encryptor.encrypt(decrypted)
                                    message_header = f"{len(msg):<{HEADER_LENGTH}}".encode('utf-8')
                                    client_socket.send(user['header'] + user['data'] + message_header + msg)

            for notified_socket in exception_sockets:
                sockets_list.remove(notified_socket)
                del clients[notified_socket]

        for x in sockets_list:
            sockets_list[x].close()
        sockets_list.clear()
        clients.clear()
        id_list=0

        log.info('Closed Sever Service')
        self.textEditor.append('[ INFO ] Closed Sever Service')
        self.actionStart.setEnabled(True)
        self.actionStop.setEnabled(False)
        return 0

    def receive_message(self, client_socket):
        try:
            message_header = client_socket.recv(HEADER_LENGTH)
            if not len(message_header):
                return False
            message_length = int(message_header.decode('utf-8').strip())
            return {'header': message_header, 'data': client_socket.recv(message_length)}
        except Exception as ex:
            log.error(str(ex))
            return False

    def send_mesage(self, message_temp):
        global clients_list_show, clients, Custom_set, PriKey
        nameserver=Custom_set['Settings']['nameserver'].encode('utf-8')
        nameserver_header = f"{len(nameserver):<{HEADER_LENGTH}}".encode('utf-8')
        message=message_temp.encode('utf-8')
        for y in clients:
            for x in clients_list_show:
                if clients_list_show[x]['sock'] == y:
                    PubKey_ex = RSA.import_key(clients_list_show[x].get('PubKey'))
                    # encryptor = PKCS1_OAEP.new(PubKey_ex)
                    # encrypted = encryptor.encrypt(message)
                    message_header = f"{len(message):<{HEADER_LENGTH}}".encode('utf-8')
                    clients_list_show[x]['sock'].send(nameserver_header + nameserver + message_header + message)
        self.textEditor.append("Message Send Compleate")
        self.textEditor.moveCursor(QtGui.QTextCursor.End)

    def kick_user(self, id_user):
        global clients_list_show, sockets_list, clients
        temp = clients_list_show[id_user]['sock']
        tmp='User Kick ID:'+str(id_user)+' Socket: {}:{}'.format(*clients_list_show[id_user]['addres'])+' Username:'+clients_list_show[id_user]['data']
        for x in sockets_list:
           if x == temp:
               x.close()
               break
        log.info(tmp)
        self.textEditor.append("[ INFO ] "+tmp)
        self.textEditor.moveCursor(QtGui.QTextCursor.End)

    def show_slit(self):
        global clients_list_show, id_list
        tmp=""
        self.textEditor.append(" ")
        self.textEditor.append("List Conneciton:")
        self.textEditor.append(" ")
        for x in clients_list_show:
            #print('ID:'+str(x)+' Socket: {}:{}'.format(*clients_list_show[x]['sock']), 'Username:', clients_list_show[x]['data'])
            tmp='ID:'+str(x)+' Socket: {}:{}'.format(*clients_list_show[x]['addres'])+' Username:'+clients_list_show[x]['data']
            self.textEditor.append(tmp)
        self.textEditor.append(" ")
        self.textEditor.moveCursor(QtGui.QTextCursor.End)
    
    def check_com(self):
        global show_print
        temp=self.lineEdit.text()
        comand=temp.split(" ",1)
        self.lineEdit.setText("")
        if comand[0] == "/list":
            self.show_slit()
            return 0
        elif comand[0] == "/start":
            self.Start_Engine()
            return 0
        elif comand[0] == "/stop":
            self.Stop_sever()
            return 0
        elif comand[0] == "/chatshow" or  comand[0] == "/showchat":
            show_print= not show_print
            if show_print == True:
                self.textEditor.append("[ INFO ] Show Chat Enable")
                self.textEditor.moveCursor(QtGui.QTextCursor.End)
            if show_print == False:
                self.textEditor.append("[ INFO ] Show Chat Disable")
                self.textEditor.moveCursor(QtGui.QTextCursor.End)
            return 0
        elif comand[0] == "/msg":
            comand.remove("/msg")
            msg=" "
            msg=msg.join(comand)
            self.send_mesage(msg)
            return 0
        elif comand[0] == "/kick":
            comand.remove("/kick")
            msg=" "
            msg=msg.join(comand)
            self.kick_user(int(msg))
            return 0
        elif comand[0] == "/help":
            self.textEditor.append(" ")
            self.textEditor.append("COMAND LIST:")
            self.textEditor.append("[   /start   ]  Start service")
            self.textEditor.append("[   /stop    ]  Stop  service")
            self.textEditor.append("[   /chatshow]  Set show chat")
            self.textEditor.append("[   /msg     ]  Send  Message to ChatRoom")
            self.textEditor.append("[   /list       ]  Show list Connetion User")
            self.textEditor.append("[   /kick      ]  kick User ChatRoom by \"ID\"")
            self.textEditor.append(" ")
            self.textEditor.moveCursor(QtGui.QTextCursor.End)
            return 0
        else:
            self.textEditor.append("Command not found check '/help'")
            self.textEditor.moveCursor(QtGui.QTextCursor.End)
            return 0
        

    #Exit
    def quit(self):
        self.Stop_server()
        sys.exit()


if __name__ == "__main__":
    import sys
    app = QtWidgets.QApplication(sys.argv)
    MainWindow = QtWidgets.QMainWindow()
    ui = Ui_MainWindow()
    ui.setupUi(MainWindow)
    MainWindow.show()
    sys.exit(app.exec_())
