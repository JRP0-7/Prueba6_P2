package Prueba6.modelo;

import java.io.File;
import java.util.ArrayList;

public class Explorador {
    private int txt = 0;
    private int java = 0;
    private int pdf = 0;
    private int otros = 0;
    private long tamTxt = 0;
    private long tamJava = 0;
    private long tamPdf = 0;
    private long tamOtros = 0;

    private String texto = "";
    private String extension = "";
    private long minKb = -1;
    private long maxKb = -1;
    private long desde = -1;
    private long hasta = -1;

    public static File validarRuta(String texto) throws RutaInvalidaException {
        File ruta = new File(texto.trim());
        if (!ruta.exists() || !ruta.isDirectory()) {
            throw new RutaInvalidaException("La ruta ingresada no existe o no es directorio");
        }
        return ruta;
    }

    public void filtrar(String texto, String extension, long minKb, long maxKb, long desde, long hasta) {
        this.texto = texto.toLowerCase();
        this.extension = extension.toLowerCase().replace(".", "");
        this.minKb = minKb;
        this.maxKb = maxKb;
        this.desde = desde;
        this.hasta = hasta;
    }

    public void contar(File f, int prof, int nivel) {
        File[] hijos = f.listFiles();
        if (hijos == null) {
            return;
        }

        for (File h : hijos) {
            if (h.isDirectory()) {
                if (prof == 0 || nivel < prof) {
                    contar(h, prof, nivel + 1);
                }
            } else {
                String nombre = h.getName().toLowerCase();
                if (nombre.endsWith(".txt")) {
                    txt++;
                    tamTxt += h.length();
                } else if (nombre.endsWith(".java")) {
                    java++;
                    tamJava += h.length();
                } else if (nombre.endsWith(".pdf")) {
                    pdf++;
                    tamPdf += h.length();
                } else {
                    otros++;
                    tamOtros += h.length();
                }
            }
        }
    }

    public String genResumen() {
        long total = tamTxt + tamJava + tamPdf + tamOtros;
        String resumen = "";
        resumen += "TXT: " + txt + " archivos, " + formato(tamTxt) + ", " + porcentaje(tamTxt, total) + "\n";
        resumen += "JAVA: " + java + " archivos, " + formato(tamJava) + ", " + porcentaje(tamJava, total) + "\n";
        resumen += "PDF: " + pdf + " archivos, " + formato(tamPdf) + ", " + porcentaje(tamPdf, total) + "\n";
        resumen += "OTROS: " + otros + " archivos, " + formato(tamOtros) + ", " + porcentaje(tamOtros, total) + "\n";
        return resumen;
    }

    private String porcentaje(long tam, long total) {
        if (total == 0) {
            return "0.0%";
        }
        return String.format("%.1f", tam * 100.0 / total) + "%";
    }

    private String formato(long bytes) {
        double kb = bytes / 1024.0;
        double mb = kb / 1024;
        double gb = mb / 1024;
        if (gb >= 1) {
            return String.format("%.2f GB", gb);
        } else if (mb >= 1) {
            return String.format("%.2f MB", mb);
        } else {
            return String.format("%.2f KB", kb);
        }
    }

    public void buscar(File f, int prof, int nivel, ArrayList<String> resultados) {
        File[] hijos = f.listFiles();
        if (hijos == null) {
            return;
        }

        for (File h : hijos) {
            if (h.isDirectory()) {
                if (prof == 0 || nivel < prof) {
                    buscar(h, prof, nivel + 1, resultados);
                }
            } else if (cumple(h)) {
                resultados.add(h.getAbsolutePath());
            }
        }
    }

    private boolean cumple(File h) {
        String nombre = h.getName().toLowerCase();
        long kb = h.length() / 1024;
        long fecha = h.lastModified();

        if (!nombre.contains(texto)) {
            return false;
        }
        if (!extension.equals("") && !nombre.endsWith("." + extension)) {
            return false;
        }
        if (minKb >= 0 && kb < minKb) {
            return false;
        }
        if (maxKb >= 0 && kb > maxKb) {
            return false;
        }
        if (desde >= 0 && fecha < desde) {
            return false;
        }
        if (hasta >= 0 && fecha > hasta) {
            return false;
        }
        return true;
    }
}
