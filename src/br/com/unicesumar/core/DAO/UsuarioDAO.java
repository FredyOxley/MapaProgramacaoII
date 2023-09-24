package br.com.unicesumar.core.DAO;

import br.com.unicesumar.core.conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import br.com.unicesumar.core.entity.Usuario;

public class UsuarioDAO {

    public boolean cadastrarUsuario(Usuario usuario) {
        try {
            Connection connection = Conexao.obterConexao();

            if (verificaUsuarioExistente(connection, usuario)) {
                JOptionPane.showMessageDialog(null, "Usuário com mesmo login ou email já existe", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String insertQuery = "INSERT INTO usuario (nome, login, senha, email) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(insertQuery);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getLogin());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getEmail());

            int rowsInserted = ps.executeUpdate();

            ps.close();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar usuário", "Erro", JOptionPane.ERROR_MESSAGE);
            }

            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar usuário", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean verificaUsuarioExistente(Connection connection, Usuario usuario) throws SQLException {
        String verificaQuery = "SELECT id FROM usuario WHERE login = ? OR email = ?";
        PreparedStatement verificaStatement = connection.prepareStatement(verificaQuery);
        verificaStatement.setString(1, usuario.getLogin());
        verificaStatement.setString(2, usuario.getEmail());

        ResultSet verificaResultSet = verificaStatement.executeQuery();

        if (verificaResultSet.next()) {
            return true;
        }

        verificaResultSet.close();
        verificaStatement.close();
        return false;
    }

    public boolean verificarLogin(Usuario usuario) {
        try {
            Connection connection = Conexao.obterConexao();
            String query = "SELECT id, nome, login, senha, email FROM usuario WHERE login = ? AND senha = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, usuario.getLogin());
            ps.setString(2, usuario.getSenha());

            ResultSet resultSet = ps.executeQuery();

            boolean loginValido = resultSet.next();

            resultSet.close();
            ps.close();
            if (loginValido) {
                JOptionPane.showMessageDialog(null, "Acesso autorizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Acesso negado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

            return loginValido;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao verificar login", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }
}
