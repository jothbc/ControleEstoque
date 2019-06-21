package EstoqueFrms;

import ModelBean.Condicao;
import ModelBean.Estoque;
import ModelBean.Produto;
import ModelBean.Usuario;
import ModelDAO.CondicaoDAO;
import ModelDAO.EstoqueDAO;
import ModelDAO.GerarLog;
import ModelDAO.ProdutoDAO;
import ModelDAO.UsuarioDAO;
import funcoes.CDbl;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import funcoes.Util;

/**
 *
 * @author User
 */
public class frmBaixasLancar extends javax.swing.JInternalFrame {

    public String user;
    private List<Produto> produtos = new ArrayList();
    private List<Estoque> estoque;
    private List<Condicao> condicoes;

    /**
     * @return Lista de Estoque totalmente preenchida;
     */
    public List<Estoque> arrumarList() {
        List<Estoque> nvEstoque = new ArrayList<>();
        for (Produto p : produtos) {
            String cd, desc;
            Double qb = null, qe = null;
            cd = p.getCodigo();
            desc = p.getDescricao();
            for (Estoque e : estoque) {
                if (e.getCd().equals(p.getCodigo())) {
                    qb = e.getQtd_baixa();
                    qe = e.getQtd_estoque();
                }
            }
            Estoque nve = new Estoque();
            nve.setDesc(desc);
            nve.setCd(cd);
            nve.setQtd_baixa(qb);
            nve.setQtd_estoque(qe);
            nvEstoque.add(nve);
        }
        return nvEstoque;
    }

    public frmBaixasLancar(String user_) {
        initComponents();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        scrnsize.height = scrnsize.height - 82;
        //scrnsize.width = this.getWidth();
        this.setSize(scrnsize);
        produtos = new ProdutoDAO().getProdutosFirebird();
        estoque = new EstoqueDAO().getEstoque();
        estoque = arrumarList();
        this.user = user_;
        qtdtxt.setFocusable(false);
        cdtxt.requestFocus();
        jProgressBar1.setVisible(false);
        painelFinalizar.setVisible(false);
        inicializarPainelFinalizar();
    }

    public void incluirTabela(String cd, Double qtd) {
        if (cd.charAt(0) == '2' && cd.length() == 13) {
            String cd_, valor_;
            cd_ = cd.substring(4, 7);
            valor_ = cd.substring(7);
            cd = Integer.toString(Integer.parseInt(cd_));
            double valor = Double.parseDouble(valor_) / 1000.00;
            for (int x = 0; x < produtos.size(); x++) {
                if (produtos.get(x).getCodigo().equals(cd)) {
                    qtd = (double) valor / produtos.get(x).getValor();
                }
            }
        }
        try {
            DefaultTableModel tb = (DefaultTableModel) jTable1.getModel();
            Produto produto = new Produto();
            produto.setCodigo(cd);
            produto.setQtd(qtd);
            for (int x = 0; x < produtos.size(); x++) {
                if (produto.getCodigo().equals(produtos.get(x).getCodigo())) {
                    produto.setDescricao(produtos.get(x).getDescricao());
                    produto.setValor(produtos.get(x).getValor());
                }
            }
            if (produto.getDescricao() == null) {
                JOptionPane.showMessageDialog(null, "Produto nao cadastrado!");
                return;
            }
            double temp_disp = 0;
            for (Estoque e : estoque) {
                if (e.getCd().equals(produto.getCodigo())) {
                    temp_disp = e.getQtd_estoque() - e.getQtd_baixa();
                    e.setQtd_estoque(e.getQtd_estoque() - produto.getQtd());
                }
            }
            disptxt.setText(Double.toString(temp_disp));
            Object[] dado = {jTable1.getRowCount() + 1, produto.getCodigo(), produto.getDescricao(), produto.getQtd()};
            tb.addRow(dado);
            qtdtxt.setText("1");
            cdtxt.setText("");
            cdtxt.requestFocus();
            desctxt.setText(produto.getDescricao());

            int linha=jTable1.getRowCount()-1;
            jTable1.setRowSelectionInterval(linha, linha);
            Util.setViewPortPosition((JViewport) jTable1.getParent(), jTable1.getCellRect(linha, 0, true));
        } catch (HeadlessException ex) {
            System.err.println("ERRO ao tentar incluir na tabela: " + ex);
        }
    }

    private void salvar(int op) {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        new Thread() {
            @Override
            public void run() {
                DefaultTableModel tb = (DefaultTableModel) jTable1.getModel();
                List<Produto> produtosJaAdicionados = new ProdutoDAO().findALL();
                boolean exist;
                jProgressBar1.setVisible(true);
                jProgressBar1.setMaximum(jTable1.getRowCount());
                jProgressBar1.setValue(0);
                try {
                    for (int x = 0; x < jTable1.getRowCount(); x++) {
                        exist = false;
                        Produto produto = new Produto();
                        produto.setCodigo((String) tb.getValueAt(x, 1));
                        produto.setQtd((Double) tb.getValueAt(x, 3));
                        produto.setTp(op);
                        produto.setDescricao((String) tb.getValueAt(x, 2));
                        for (int i = 0; i < produtosJaAdicionados.size(); i++) {
                            if (produtosJaAdicionados.get(i).getCodigo().equals(produto.getCodigo()) && produtosJaAdicionados.get(i).getTp() == produto.getTp()) {
                                double total = produtosJaAdicionados.get(i).getQtd() + produto.getQtd();
                                produto.setQtd(total);
                                produto.setId(produtosJaAdicionados.get(i).getId());
                                exist = true;
                            }
                        }
                        if (exist) {
                            if (!new ProdutoDAO().updateQTD(produto)) {
                                throw new Exception("Erro no update: ");
                            } else {
                                if (!new GerarLog().lancarLogBaixa(produto, (Double) tb.getValueAt(x, 3), user, condicoes)) {
                                    JOptionPane.showMessageDialog(null, "Update realizado com sucesso, porem foi gerado um erro ao tentar salvar o log no banco de dados!\nProduto: " + produto.getDescricao());
                                }
                                jProgressBar1.setValue(x + 1);
                            }

                        } else {
                            if (!new ProdutoDAO().add(produto)) {
                                throw new Exception("Erro no add: ");
                            } else {
                                if (!new GerarLog().lancarLogBaixa(produto, (Double) tb.getValueAt(x, 3), user, condicoes)) {
                                    JOptionPane.showMessageDialog(null, "Adição realizada com sucesso, porem foi gerado um erro ao tentar salvar o log no banco de dados!\nProduto: " + produto.getDescricao());
                                }
                                jProgressBar1.setValue(x + 1);
                            }
                        }
                        produtosJaAdicionados.clear();
                        produtosJaAdicionados = new ProdutoDAO().findALL();
                    }
                    tb.setRowCount(0);
                    jProgressBar1.setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao tentar salvar: " + ex, "ERRO", JOptionPane.ERROR_MESSAGE);
                }
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            }
        }.start();
    }

    public void verificarKey(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_F5 && jTable1.getRowCount() > 0) {  //finalizar selecionando Condicao
            finalizar();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F3) {                               //atualiza produtos vindos do firebird
            atualizarList();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F4) {                               //focus na quantidade
            qtdtxt.setFocusable(true);
            qtdtxt.requestFocus();
            qtdtxt.setText("");
        }
        if (evt.getKeyCode() == KeyEvent.VK_F6) {                               //exclui item
            excluirItem();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {                            //inclui na tabela
            if (cdtxt.isFocusOwner()) {
                qtdtxt.setText(qtdtxt.getText().replaceAll(",", "."));
                incluirTabela(cdtxt.getText(), CDbl.CDblTresCasas(Double.parseDouble(qtdtxt.getText())));
            } else {
                cdtxt.requestFocus();
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_F12) {                              //buscar movimento
            buscarMovimento();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F1) {                               //abre o jDialog de Ajuda
            jdHelp jd = new jdHelp(null, true);
            jd.setVisible(true);
        }
        if (evt.getKeyCode() == KeyEvent.VK_F2) {                               //localizar Produto
            localizarProduto();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        disptxt = new javax.swing.JTextField();
        painelFinalizar = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        qtdtxt = new javax.swing.JTextField();
        cdtxt = new javax.swing.JTextField();
        desctxt = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();

        setClosable(true);
        setMaximizable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jTable1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sequência", "Código", "Descrição", "Quantidade"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        jTable1.setAutoscrolls(false);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "Comandos"));

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Finalizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 51, 153));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 51, 153));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Buscar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 51, 153));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Atualizar Produtos");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 51, 153));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Buscar Movimento");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)), "Quantidade Disponivel"));

        disptxt.setEditable(false);
        disptxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        disptxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(disptxt)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(disptxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        painelFinalizar.setBackground(new java.awt.Color(102, 102, 102));
        painelFinalizar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)), "Tipo de Baixa"));

        jButton6.setBackground(new java.awt.Color(0, 204, 0));
        jButton6.setText("Finalizar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Condição"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
        }

        javax.swing.GroupLayout painelFinalizarLayout = new javax.swing.GroupLayout(painelFinalizar);
        painelFinalizar.setLayout(painelFinalizarLayout);
        painelFinalizarLayout.setHorizontalGroup(
            painelFinalizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        painelFinalizarLayout.setVerticalGroup(
            painelFinalizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelFinalizarLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6))
        );

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Código");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quantidade");

        qtdtxt.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        qtdtxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        qtdtxt.setText("1");
        qtdtxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qtdtxtMouseClicked(evt);
            }
        });
        qtdtxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtdtxtKeyPressed(evt);
            }
        });

        cdtxt.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        cdtxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cdtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cdtxtActionPerformed(evt);
            }
        });
        cdtxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cdtxtKeyPressed(evt);
            }
        });

        desctxt.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        desctxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        desctxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desctxtActionPerformed(evt);
            }
        });
        desctxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                desctxtKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(desctxt)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(qtdtxt)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cdtxt)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qtdtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cdtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desctxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jProgressBar1.setToolTipText("Salvando, não feche o programa!");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(painelFinalizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(painelFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cdtxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cdtxtKeyPressed
        // TODO add your handling code here:
        verificarKey(evt);
    }//GEN-LAST:event_cdtxtKeyPressed

    private void cdtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cdtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cdtxtActionPerformed

    private void qtdtxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtdtxtKeyPressed
        // TODO add your handling code here:
        verificarKey(evt);
    }//GEN-LAST:event_qtdtxtKeyPressed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:
        verificarKey(evt);
    }//GEN-LAST:event_jTable1KeyPressed

    private void qtdtxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qtdtxtMouseClicked
        // TODO add your handling code here:
        qtdtxt.setFocusable(true);
    }//GEN-LAST:event_qtdtxtMouseClicked

    private void desctxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desctxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_desctxtActionPerformed

    private void desctxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_desctxtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_desctxtKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        finalizar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        excluirItem();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        localizarProduto();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        atualizarList();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        buscarMovimento();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel tb2 = (DefaultTableModel) jTable2.getModel();
        int op = -1;
        if (jTable2.getSelectedRow() >= 0) {
            for (Condicao c : condicoes) {
                if (c.getDecricao().equals(tb2.getValueAt(jTable2.getSelectedRow(), 0))) {
                    op = c.getTp();
                    if (op > -1) {
                        salvar(op);
                    }
                    painelFinalizar.setVisible(false);
                }
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        //logoini();
    }//GEN-LAST:event_formInternalFrameOpened


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cdtxt;
    private javax.swing.JTextField desctxt;
    private javax.swing.JTextField disptxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JPanel painelFinalizar;
    private javax.swing.JTextField qtdtxt;
    // End of variables declaration//GEN-END:variables

    private void finalizar() {
        List<Usuario> users = new UsuarioDAO().findAll();
        for (int x = 0; x < users.size(); x++) {
            if (user.equals(users.get(x).getNome()) && users.get(x).getPermissao() == 1) {
                painelFinalizar.setVisible(true);
                return;
                /*SelectCondicao selectCond = new SelectCondicao(null, true);
                selectCond.setVisible(true);
                if (selectCond.op > -1) {
                    salvar(selectCond.op);
                }
                return;
                 */
            }
        }
        jdLiberarCaixa jd = new jdLiberarCaixa(null, true);
        jd.setVisible(true);
        if (jd.op == 1) {
            painelFinalizar.setVisible(true);
            /*SelectCondicao selectCond = new SelectCondicao(null, true);
            selectCond.setVisible(true);
            if (selectCond.op > 0 && selectCond.op < 5) {
                salvar(selectCond.op);
            }*/
        }
    }

    private void atualizarList() {
        List<Produto> atualizar;
        atualizar = new ProdutoDAO().getProdutosFirebird();
        if (atualizar != null) {
            JOptionPane.showMessageDialog(null, "Tabela atualizada!", "SUCESSO!", JOptionPane.INFORMATION_MESSAGE);
            produtos.clear();
            produtos = atualizar;
        } else {
            JOptionPane.showMessageDialog(null, "Falha ao buscar novos produtos no servidor!");
        }
    }

    private void excluirItem() {
        jdExcluirProduto jd = new jdExcluirProduto(null, true);
        jd.setVisible(true);
        DefaultTableModel table = (DefaultTableModel) jTable1.getModel();
        if (jd.op == 1) {
            double temp = (double) table.getValueAt(jTable1.getRowCount() - 1, 3);
            String cd = (String) table.getValueAt(jTable1.getRowCount() - 1, 1);
            for (Estoque e : estoque) {
                if (e.getCd().equals(cd)) {
                    e.setQtd_estoque(e.getQtd_estoque() + temp);
                }
            }
            table.removeRow(jTable1.getRowCount() - 1);

        } else if (jd.op == 2) {
            if (jTable1.getSelectedRow() >= 0) {
                double temp = (double) table.getValueAt(jTable1.getSelectedRow(), 3);
                String cd = (String) table.getValueAt(jTable1.getSelectedRow(), 1);
                for (Estoque e : estoque) {
                    if (e.getCd().equals(cd)) {
                        e.setQtd_estoque(e.getQtd_estoque() + temp);
                    }
                }
                table.removeRow(jTable1.getSelectedRow());
            } else {
                JOptionPane.showMessageDialog(null, "Para remover por seleção, primeiro selecione o item na tabela.");
            }
        }
    }

    private void buscarMovimento() {
        jdCodigoMovimento jd = new jdCodigoMovimento(null, true);
        jd.setVisible(true);
        if (jd.confirma) {
            List<Produto> produtosCupom = jd.produtos;
            for (int x = 0; x < produtosCupom.size(); x++) {
                incluirTabela(produtosCupom.get(x).getCodigo(), produtosCupom.get(x).getQtd());
            }
        }
    }

    private void localizarProduto() {
        jdLocalizarProduto jd = new jdLocalizarProduto(null, true);
        jd.setVisible(true);
        if (jd.cd != null) {
            cdtxt.setText(jd.cd);
        }
    }

    private void inicializarPainelFinalizar() {
        condicoes = new CondicaoDAO().findAll();
        DefaultTableModel tb2;
        tb2 = (DefaultTableModel) jTable2.getModel();
        for (Condicao c : condicoes) {
            Object[] dado = {c.getDecricao()};
            tb2.addRow(dado);
        }
    }

    @Deprecated
    private void logoini() {
        /*ImageIcon icon = new ImageIcon("scr/Imagens/jcr.png");
        System.out.println(logo.getWidth() + "\n"+logo.getHeight());
        icon.setImage(icon.getImage().getScaledInstance(logo.getWidth(),logo.getHeight(),1));
        logo.setIcon(icon);
         */
    }
}
