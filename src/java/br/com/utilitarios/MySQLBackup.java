package br.com.utilitarios;

import br.com.entidades.Constantes;
import static br.com.utilitarios.MetodosUtil.getDateTimeSystem;
import java.util.*;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLBackup {

    private final List<String> dbList = new ArrayList<>();

    public boolean fazerBackup() {

        String data = getDateTimeSystem();

        File diretorio = new File(Constantes.CAMINHO_BACKUP);
        if (!diretorio.exists()) {
            diretorio.mkdirs();

        }

        String command = Constantes.CAMINHO_MYSQL + "mysqldump.exe";

        String[] databases = Constantes.DATABASES.split(" ");

        dbList.addAll(Arrays.asList(databases));

        // Mostra apresentação
        System.out.println("Iniciando backups...\n\n");

        // Contador
        int i = 1;

        // Tempo
        long time1, time2, time;

        // Início
        time1 = System.currentTimeMillis();

        for (String dbName : dbList) {

            ProcessBuilder pb = new ProcessBuilder(
                    command,
                    "--user=root",
                    "--password=",
                    dbName,
                    "--result-file=" + Constantes.CAMINHO_BACKUP + dbName + "_" + data + ".sql");

            try {

                System.out.println(
                        "Backup do banco de dados (" + i + "): " + dbName + " ...");

                pb.start();
            } catch (Exception e) {
                Logger.getLogger(MySQLBackup.class.getName()).log(Level.SEVERE, null, e);
            }

            i++;

        }

        // Fim
        time2 = System.currentTimeMillis();

        // Tempo total da operação
        time = time2 - time1;

        // Avisa do sucesso
        System.out.println("\nBackups realizados com sucesso.\n\n");

        System.out.println("Tempo total de processamento: " + time + " ms\n");

        System.out.println("Finalizando...");

        try {

            // Paralisa por 2 segundos
            Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            Logger.getLogger(MySQLBackup.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }

    }

}
