/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelBean;

/**
 *
 * @author User
 */
public class Usuario {
    int seq;
    String nome;
    String senha;
    int permissao;
    
    public Usuario() {
    }

    public Usuario (String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getSeq() {
        return seq;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getPermissao() {
        return permissao;
    }

    public void setPermissao(int permissao) {
        this.permissao = permissao;
    }
    
    
}
