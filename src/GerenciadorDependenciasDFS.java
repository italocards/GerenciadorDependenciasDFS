import java.util.*;

public class GerenciadorDependenciasDFS {

    private Map<String, List<String>> grafo;
    private Set<String> visitados;
    private Set<String> visitando;
    private List<String> ordemInstalacao;

    public GerenciadorDependenciasDFS() {
        grafo = new HashMap<>();
    }

    public void adicionarDependencia(String biblioteca, String dependencia) {
        grafo.putIfAbsent(biblioteca, new ArrayList<>());
        grafo.putIfAbsent(dependencia, new ArrayList<>());
        grafo.get(biblioteca).add(dependencia);
    }

    public List<String> ordenarInstalacao() {
        visitados = new HashSet<>();
        visitando = new HashSet<>();
        ordemInstalacao = new ArrayList<>();

        for (String biblioteca : grafo.keySet()) {
            if (!visitados.contains(biblioteca)) {
                if (!dfs(biblioteca)) {
                    throw new RuntimeException("Ciclo detectado nas dependências!");
                }
            }
        }

        Collections.reverse(ordemInstalacao);
        return ordemInstalacao;
    }

    private boolean dfs(String biblioteca) {
        if (visitando.contains(biblioteca)) {
            return false; // Ciclo detectado
        }

        if (visitados.contains(biblioteca)) {
            return true; // Já foi visitado
        }

        visitados.add(biblioteca);
        visitando.add(biblioteca);

        for (String dependencia : grafo.get(biblioteca)) {
            if (!dfs(dependencia)) {
                return false;
            }
        }

        visitando.remove(biblioteca);
        ordemInstalacao.add(biblioteca);
        return true;
    }

    public static void main(String[] args) {
        GerenciadorDependenciasDFS gerenciador = new GerenciadorDependenciasDFS();

        gerenciador.adicionarDependencia("express", "body-parser");
        gerenciador.adicionarDependencia("express", "cookie-parser");
        gerenciador.adicionarDependencia("express", "debug");
        gerenciador.adicionarDependencia("body-parser", "bytes");
        gerenciador.adicionarDependencia("cookie-parser", "cookie");

        try {
            List<String> ordem = gerenciador.ordenarInstalacao();
            System.out.println("Ordem de instalação:");
            for (String pkg : ordem) {
                System.out.println("npm install " + pkg);
            }
        } catch (RuntimeException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}