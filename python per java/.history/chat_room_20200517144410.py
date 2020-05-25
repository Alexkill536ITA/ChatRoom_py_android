# This Python file uses the following encoding: utf-8
import sys
import threading
import time
import socket
import select
import errno
import queue
from PyQt5.QtWidgets import QApplication, QMainWindow, QDialog
from PyQt5 import QtCore, QtGui, QtWidgets
from Dialog_Connect import Ui_Dialog
from Crypto.PublicKey import RSA
from Crypto import Random
from Crypto.Cipher import PKCS1_OAEP

HEADER_LENGTH = 10

IP = "127.0.0.1"
PORT = 1234
my_username = "Anonymus"
send_clik = False
text_int = ""
list_t = []
chat_main = ""
client_socket = ""
stop_t = False
libero = True
PriKey = ""
PubKey = ""
Server_key = ""

def Default_globlas():
    global IP, PORT, my_username, send_clik, text_int, list_t, chat_main, client_socket, stop_t, libero, PriKey, PubKey, Server_key
    IP = "127.0.0.1"
    PORT = 1234
    my_username = "Anonymus"
    send_clik = False
    text_int = ""
    list_t = []
    chat_main = ""
    client_socket = ""
    stop_t = False
    libero = True
    PriKey = ""
    PubKey = ""
    Server_key = ""


class Dialog_S(QDialog, Ui_Dialog):
    def __init__(self, parent=None):
        QDialog.__init__(self, parent)
        self.setupUi(self)

class Chat_room(object):
    def __init__(self):
        super().__init__()

    def setupUi(self, MainWindow):
        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(820, 630)
        MainWindow.setMinimumSize(QtCore.QSize(820, 630))
        MainWindow.setMaximumSize(QtCore.QSize(820, 630))
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.gridLayout = QtWidgets.QGridLayout(self.centralwidget)
        self.gridLayout.setObjectName("gridLayout")
        self.horizontalLayout = QtWidgets.QHBoxLayout()
        self.horizontalLayout.setContentsMargins(-1, -1, -1, 0)
        self.horizontalLayout.setObjectName("horizontalLayout")
        self.textResults = QtWidgets.QTextEdit(self.centralwidget)
        self.textResults.setMinimumSize(QtCore.QSize(700, 100))
        self.textResults.setMaximumSize(QtCore.QSize(16777215, 200))
        self.textResults.setAcceptDrops(False)
        self.textResults.setStyleSheet("")
        self.textResults.setUndoRedoEnabled(False)
        self.textResults.setReadOnly(False)
        self.textResults.setObjectName("textResults")
        self.horizontalLayout.addWidget(self.textResults)
        self.verticalLayout = QtWidgets.QVBoxLayout()
        self.verticalLayout.setContentsMargins(-1, -1, 0, -1)
        self.verticalLayout.setObjectName("verticalLayout")
        spacerItem = QtWidgets.QSpacerItem(
            20, 40, QtWidgets.QSizePolicy.Minimum, QtWidgets.QSizePolicy.Expanding)
        self.verticalLayout.addItem(spacerItem)
        self.pushButton = QtWidgets.QPushButton(self.centralwidget)
        self.pushButton.setObjectName("pushButton")
        self.pushButton.setEnabled(False)
        self.verticalLayout.addWidget(self.pushButton)
        self.pushButton_2 = QtWidgets.QPushButton(self.centralwidget)
        self.pushButton_2.setObjectName("pushButton_2")
        self.verticalLayout.addWidget(self.pushButton_2)
        spacerItem1 = QtWidgets.QSpacerItem(
            20, 40, QtWidgets.QSizePolicy.Minimum, QtWidgets.QSizePolicy.Expanding)
        self.verticalLayout.addItem(spacerItem1)
        self.horizontalLayout.addLayout(self.verticalLayout)
        self.gridLayout.addLayout(self.horizontalLayout, 1, 0, 1, 1)
        self.textEditor = QtWidgets.QTextEdit(self.centralwidget)
        self.textEditor.setMinimumSize(QtCore.QSize(800, 420))
        palette = QtGui.QPalette()
        brush = QtGui.QBrush(QtGui.QColor(248, 248, 242))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Text, brush)
        brush = QtGui.QBrush(QtGui.QColor(39, 40, 34))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active, QtGui.QPalette.Base, brush)
        brush = QtGui.QBrush(QtGui.QColor(248, 248, 242, 128))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Active,
                         QtGui.QPalette.PlaceholderText, brush)
        brush = QtGui.QBrush(QtGui.QColor(248, 248, 242))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Text, brush)
        brush = QtGui.QBrush(QtGui.QColor(39, 40, 34))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive, QtGui.QPalette.Base, brush)
        brush = QtGui.QBrush(QtGui.QColor(248, 248, 242, 128))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Inactive,
                         QtGui.QPalette.PlaceholderText, brush)
        brush = QtGui.QBrush(QtGui.QColor(120, 120, 120))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Text, brush)
        brush = QtGui.QBrush(QtGui.QColor(240, 240, 240))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled, QtGui.QPalette.Base, brush)
        brush = QtGui.QBrush(QtGui.QColor(0, 0, 0, 128))
        brush.setStyle(QtCore.Qt.SolidPattern)
        palette.setBrush(QtGui.QPalette.Disabled,
                         QtGui.QPalette.PlaceholderText, brush)
        self.textEditor.setPalette(palette)
        self.textEditor.setAutoFillBackground(False)
        self.textEditor.setUndoRedoEnabled(False)
        self.textEditor.setReadOnly(True)
        self.textEditor.setObjectName("textEditor")
        self.gridLayout.addWidget(self.textEditor, 0, 0, 1, 1)
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QtWidgets.QMenuBar(MainWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 820, 21))
        self.menubar.setObjectName("menubar")
        self.menuFile = QtWidgets.QMenu(self.menubar)
        self.menuFile.setObjectName("menuFile")
        self.menuInfo = QtWidgets.QMenu(self.menubar)
        self.menuInfo.setObjectName("menuInfo")
        MainWindow.setMenuBar(self.menubar)
        self.statusbar = QtWidgets.QStatusBar(MainWindow)
        self.statusbar.setObjectName("statusbar")
        MainWindow.setStatusBar(self.statusbar)
        self.toolBar = QtWidgets.QToolBar(MainWindow)
        self.toolBar.setEnabled(True)
        self.toolBar.setMovable(False)
        self.toolBar.setObjectName("toolBar")
        MainWindow.addToolBar(QtCore.Qt.TopToolBarArea, self.toolBar)
        self.actionConnect = QtWidgets.QAction(MainWindow)
        icon = QtGui.QIcon()
        icon.addPixmap(QtGui.QPixmap("Icon/Connect_sever.png"),
                       QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionConnect.setIcon(icon)
        self.actionConnect.setObjectName("actionConnect")
        self.actionDisconnect = QtWidgets.QAction(MainWindow)
        self.actionDisconnect.setEnabled(False)
        icon1 = QtGui.QIcon()
        icon1.addPixmap(QtGui.QPixmap("Icon/Disconnect_sever.png"),
                        QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionDisconnect.setIcon(icon1)
        self.actionDisconnect.setObjectName("actionDisconnect")
        self.actionClear_cli = QtWidgets.QAction(MainWindow)
        icon4 = QtGui.QIcon()
        icon4.addPixmap(QtGui.QPixmap(
            "Icon/iconfinder_terminal_clear.png"), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionClear_cli.setIcon(icon4)
        self.actionClear_cli.setObjectName("actionClear_cli")
        self.actionExit = QtWidgets.QAction(MainWindow)
        icon5 = QtGui.QIcon()
        icon5.addPixmap(QtGui.QPixmap("Icon/shell32_240.ico"),
                        QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionExit.setIcon(icon5)
        self.actionExit.setObjectName("actionExit")
        self.actionAbout_Qt5 = QtWidgets.QAction(MainWindow)
        icon6 = QtGui.QIcon()
        icon6.addPixmap(QtGui.QPixmap("Icon/qt_94938.ico"),
                        QtGui.QIcon.Normal, QtGui.QIcon.Off)
        self.actionAbout_Qt5.setIcon(icon6)
        self.actionAbout_Qt5.setObjectName("actionAbout_Qt5")
        self.actionAbout_Chat_Room = QtWidgets.QAction(MainWindow)
        self.actionAbout_Chat_Room.setObjectName("actionAbout_Chat_Room")
        self.menuFile.addAction(self.actionConnect)
        self.menuFile.addAction(self.actionDisconnect)
        self.menuFile.addSeparator()
        self.menuFile.addAction(self.actionExit)
        self.menuInfo.addAction(self.actionAbout_Qt5)
        self.menuInfo.addAction(self.actionAbout_Chat_Room)
        self.menubar.addAction(self.menuFile.menuAction())
        self.menubar.addAction(self.menuInfo.menuAction())
        self.toolBar.addSeparator()
        self.toolBar.addAction(self.actionConnect)
        self.toolBar.addAction(self.actionDisconnect)
        self.toolBar.addSeparator()
        self.toolBar.addAction(self.actionClear_cli)

        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

        self.actionConnect.triggered.connect(self.Connect_dialog)
        self.actionClear_cli.triggered.connect(self.chat_clear)
        self.actionExit.triggered.connect(self.quit)
        self.pushButton.clicked.connect(self.clik_on)
        self.pushButton_2.clicked.connect(self.intbox_clear)
        self.actionDisconnect.triggered.connect(self.Disconnect_def)
    
    def chat_clear(self):
        self.textEditor.setText("")

    def intbox_clear(self):
        self.textResults.setText("")

    def clik_on(self):
        global send_clik, text_int
        send_clik = True
        text_int = self.textResults.toPlainText()
        self.textResults.setText("")
        fin = my_username+" > "+text_int
        self.textEditor.append(fin)

    def Recive_def(self, client_main):
        global stop_t
        while True:
            if stop_t == False:
                try:
                    # Ora vogliamo scorrere i messaggi ricevuti (potrebbe essercene più di uno) e stamparli
                    while True:
                            
                        # Ricevi la nostra "intestazione" contenente la lunghezza del nome utente, la sua dimensione è definita e costante
                        username_header = client_main.recv(HEADER_LENGTH)

                        # Se non abbiamo ricevuto dati, il server ha chiuso con grazia una connessione, ad esempio utilizzando socket.close () o socket.shutdown (socket.SHUT_RDWR)
                        if not len(username_header):
                            print('Connection closed by the server')
                            self.textEditor.append("[    INFO ]: Connection closed by the server")
                            sys.exit()

                        # Converti l'intestazione in valore int
                        username_length = int(username_header.decode('utf-8').strip())

                        # Ricevi e decodifica il nome utente
                        username = client_main.recv(username_length).decode('utf-8')

                        # Ora fai lo stesso per il messaggio (dato che abbiamo ricevuto il nome utente, abbiamo ricevuto l'intero messaggio, non è necessario verificare se ha una lunghezza)
                        message_header = client_main.recv(HEADER_LENGTH)
                        message_length = int(message_header.decode('utf-8').strip())
                        message = client_main.recv(message_length)
                        # decryptor = PKCS1_OAEP.new(PriKey)
                        # decrypted = decryptor.decrypt(message)

                        # Stampa messaggio
                        # print(f'{username} > {message}')
                        fin = username+" > "+message.decode()
                        self.textEditor.append(fin)

                except IOError as e:
                    # Questo è normale per le connessioni non bloccanti - quando non ci sono errori di dati in arrivo verrà generato
                    # Alcuni sistemi operativi indicano che l'utilizzo di AGAIN e altri l'utilizzo del codice di errore WOULDBLOCK
                    # Verificheremo entrambi - se uno di questi - è previsto, significa che non ci sono dati in arrivo, continua normalmente
                    # Se abbiamo un codice di errore diverso, è successo qualcosa
                    if e.errno != errno.EAGAIN and e.errno != errno.EWOULDBLOCK:
                        print('Reading error: {}'.format(str(e)))
                        self.textEditor.append('[ ERROR ]: {}'.format(str(e)))
                        stop_t = True
                        return 1

                    # Non abbiamo ricevuto nulla
                    continue

                except Exception as e:
                    # Qualsiasi altra eccezione: è successo qualcosa, esci
                    print('Reading error: '.format(str(e)))
                    # self.textEditor.append('[ ERROR ]: {}'.format(str(e)))
            else:
                return 0
                        
    def send_def(self, client_main):
        global send_clik, text_int, stop_t, Server_key
        while True:
            if stop_t == False:
                # Attendere che l'utente inserisca un messaggio
                #message = input(f'{my_username} > ')
                if(send_clik == True):
                    message = text_int

                    # Se il messaggio non è vuoto, inviarlo
                    if message:

                        # Codifica il messaggio in byte, prepara l'intestazione e converti in byte, come per il nome utente sopra, quindi invia
                        message = message.encode()
                        # encryptor = PKCS1_OAEP.new(Server_key)
                        # encrypted = encryptor.encrypt(message)
                        message_header = f"{len(message):<{HEADER_LENGTH}}".encode('utf-8')
                        client_main.send(message_header + message)
                        send_clik = False
                    send_clik = False
            else:
                return 0

    def Connect_dialog(self):
        global IP, PORT, my_username, list_t, chat_main, libero, stop_t
        Dialog = Dialog_S()
        Dialog.show()
        rsp = Dialog.exec_()
        if libero == False:
            self.Disconnect_def()
        Default_globlas()
        self.textEditor.setText("")
        if rsp == QtWidgets.QDialog.Accepted:
            print(Dialog.lineEdit_Username.text())
            print(Dialog.lineEdit_Password.text())
            port_temp = Dialog.lineEdit.text()
            if(len(Dialog.lineEdit_Username.text())>0):
                my_username = Dialog.lineEdit_Username.text()
            if (len(port_temp) > 0 and port_temp.isdigit() == True):
                if(int(port_temp) >= 1024 and int(port_temp) <= 65536):
                    PORT = int(port_temp)
            else:
                mes =QtWidgets.QMessageBox()
                mes.setWindowTitle("Errore")
                mes.setText("Porta del Server non Valida")
                mes.setIcon(QtWidgets.QMessageBox.Warning)
                mes.setStandardButtons(QtWidgets.QMessageBox.Ok)
                mes.exec_()
                
            if(len(Dialog.lineEdit_Password.text())>6):
                IP = Dialog.lineEdit_Password.text()
                chat_main = threading.Thread(target=(self.start_chat), args=())
                chat_main.start()
                if chat_main == 0:
                    chat_main.join()
            else:
                mes =QtWidgets.QMessageBox()
                mes.setWindowTitle("Errore")
                mes.setText("L'indirizzo del Server non Valido")
                mes.setIcon(QtWidgets.QMessageBox.Warning)
                mes.setStandardButtons(QtWidgets.QMessageBox.Ok)
                mes.exec_()        
        return 0

    def quit(self):
        self.Disconnect_def()
        sys.exit()

    def retranslateUi(self, MainWindow):
        _translate = QtCore.QCoreApplication.translate
        MainWindow.setWindowTitle(_translate("MainWindow", "Chat Room"))
        self.pushButton.setText(_translate("MainWindow", "Send"))
        self.pushButton.setShortcut(_translate("MainWindow", "Return"))
        self.pushButton_2.setText(_translate("MainWindow", "Clear"))
        self.menuFile.setTitle(_translate("MainWindow", "File"))
        self.menuInfo.setTitle(_translate("MainWindow", "Info"))
        self.toolBar.setWindowTitle(_translate("MainWindow", "toolBar"))
        self.actionConnect.setText(_translate("MainWindow", "Connect"))
        self.actionConnect.setToolTip(_translate("MainWindow", "Connect"))
        self.actionConnect.setShortcut(_translate("MainWindow", "Ctrl+N"))
        self.actionDisconnect.setText(_translate("MainWindow", "Disconnect"))
        self.actionDisconnect.setToolTip(_translate("MainWindow", "Disconnect"))
        self.actionDisconnect.setShortcut(_translate("MainWindow", "Ctrl+D"))
        self.actionClear_cli.setText(_translate("MainWindow", "Clear"))
        self.actionClear_cli.setToolTip(_translate("MainWindow", "Clear cli"))
        self.actionClear_cli.setShortcut(_translate("MainWindow", "Ctrl+L"))
        self.actionExit.setText(_translate("MainWindow", "Exit"))
        self.actionExit.setToolTip(_translate("MainWindow", "Exit"))
        self.actionExit.setShortcut(_translate("MainWindow", "Ctrl+Q"))
        self.actionAbout_Qt5.setText(_translate("MainWindow", "About Qt5"))
        self.actionAbout_Chat_Room.setText(_translate("MainWindow", "About Chat Room"))
        self.actionAbout_Chat_Room.setShortcut(_translate("MainWindow", "Ctrl+I"))

    # RSA Key
    def Generate_Key(self):
        global PriKey, PubKey
        random_generator = Random.new().read
        PriKey = RSA.generate(1024, random_generator)

        PubKey = PriKey.publickey()
        # print(f"Public key:  (n={hex(pubKey.n)}, e={hex(pubKey.e)})")
        PubKey = PubKey.exportKey()
        # print(pubKeyPEM.decode('ascii'))

        # print(f"Private key: (n={hex(pubKey.n)}, d={hex(keyPair.d)})")
        privKeyPEM = PriKey.exportKey()
        # print(privKeyPEM.decode('ascii'))

    def start_chat(self):
            global IP, PORT, my_username, list_t, client_socket, libero, stop_t, PubKey, PriKey, Server_key
            self.Generate_Key()
            self.textEditor.append("[    INFO ]: Connecting...")
            client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)            
            try:
                client_socket.connect((IP, PORT))
                client_socket.setblocking(False)                   
            except IOError as e:
                if e.errno != errno.EAGAIN and e.errno != errno.EWOULDBLOCK:
                    print('[ ERROR ]: {}'.format(str(e)))
                    self.textEditor.append('[ ERROR ]: {}'.format(str(e)))
                    return 0
            libero = False

            username = my_username.encode('utf-8')
            username_header = f"{len(username):<{HEADER_LENGTH}}".encode('utf-8')
            client_socket.send(username_header + username)
            time.sleep(3)
            username_header = client_socket.recv(HEADER_LENGTH)
            if not len(username_header):
                print('Connection closed by the server')
                sys.exit()
            username_length = int(username_header.decode('utf-8').strip())
            username = client_socket.recv(username_length).decode('utf-8')
            message_header = client_socket.recv(HEADER_LENGTH)
            message_length = int(message_header.decode('utf-8').strip())
            message = client_socket.recv(message_length)
            Server_key = RSA.import_key(message)
            
            message = PubKey 
            message_header = f"{len(message):<{HEADER_LENGTH}}".encode('utf-8')
            client_socket.send(message_header + message)

            self.textEditor.append("[    INFO ]: Connect To "+IP+":"+str(PORT))
            self.textEditor.append("______________________________________\n")
            self.textEditor.append("|    Welcom To Chat Room By AlexKill536ITA    |")
            self.textEditor.append("______________________________________\n")
            self.actionDisconnect.setEnabled(True)
            self.pushButton.setEnabled(True)
            send_t = threading.Thread(target=(self.send_def), args=(client_socket,))
            send_t.start()
            list_t.append(send_t)
            recive_t = threading.Thread(target=(self.Recive_def), args=(client_socket,))
            recive_t.start()   
            list_t.append(recive_t)
            return 0
    
    def Disconnect_def(self):
        global list_t, chat_main, client_socket, stop_t, libero
        stop_t = True
        for t in list_t:
            t.join()
        client_socket.close()
        chat_main.join()
        libero = True
        self.actionDisconnect.setEnabled(False)
        self.pushButton.setEnabled(False)
        stop_t = False


if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)
    MainWindow = QtWidgets.QMainWindow()
    window = Chat_room()
    window.setupUi(MainWindow)
    MainWindow.show()
    sys.exit(app.exec_())
