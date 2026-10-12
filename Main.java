import java.io.*;


class Nodo {

    private String valor;
    private String tipo;
    private Nodo siguiente;

    public Nodo(String valor, String tipo) {
        this.valor = valor;
        this.tipo = tipo;
        this.siguiente = null;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}


class Pila {

    private Nodo cima;
    private int tamanio;

    public Pila() {
        cima = null;
        tamanio = 0;
    }

    public void push(String valor, String tipo) {
        Nodo nuevo = new Nodo(valor, tipo);
        nuevo.setSiguiente(cima);
        cima = nuevo;
        tamanio++;
    }

    public Nodo pop() {
        if (estaVacia()) {
            return null;
        }
        Nodo eliminado = cima;
        cima = cima.getSiguiente();
        eliminado.setSiguiente(null);
        tamanio--;
        return eliminado;
    }

    public Nodo peek() {
        return cima;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void vaciar() {
        cima = null;
        tamanio = 0;
    }

    public String enLinea() {
        String texto = "[";
        Nodo actual = cima;
        while (actual != null) {
            texto += actual.getValor();
            if (actual.getSiguiente() != null) {
                texto += ", ";
            }
            actual = actual.getSiguiente();
        }
        return texto + "]";
    }

    public String mostrar() {
        if (estaVacia()) {
            return "La pila esta vacia.";
        }
        String dibujo = "";
        Nodo actual = cima;
        boolean esCima = true;
        while (actual != null) {
            dibujo += String.format("   | %-10s|  %s", actual.getValor(), actual.getTipo());
            if (esCima) {
                dibujo += "   <- CIMA";
                esCima = false;
            }
            dibujo += "\n";
            actual = actual.getSiguiente();
        }
        dibujo += "   +-----------+\n";
        dibujo += "   Elementos: " + tamanio;
        return dibujo;
    }
}


public class Main {

    static String[] nombresVariables = new String[50];
    static double[] valoresVariables = new double[50];
    static int cantidadVariables = 0;

    public static void main(String []args) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    PrintStream out = System.out;

    Pila pila = new Pila();
    int opcion = -1;

    
    out.println("  ANALIZADOR DE EXPRESIONES ARITMETICAS");
    out.println("  usando una PILA ");
   
    while (opcion != 0) {
        menu(out);
        opcion = leerOpcion(in);

        switch (opcion) {

            case 1:
                out.print("Digite el token a insertar (numero, variable, operador o parentesis): ");
                String token = leerLinea(in);
                String tipo = tipoDeToken(token);
                if (tipo.equals("INVALIDO")) {
                    out.println("Error: '" + token + "' no es un token valido.");
                } else {
                    pila.push(token, tipo);
                    out.println("Se inserto '" + token + "' (" + tipo + ") en la cima.");
                }
                break;

            case 2:
                if (pila.estaVacia()) {
                    out.println("No se puede hacer POP: la pila esta vacia.");
                } else {
                    Nodo sacado = pila.pop();
                    out.println("Se saco '" + sacado.getValor() + "' (" + sacado.getTipo() + ").");
                }
                break;

            case 3:
                if (pila.estaVacia()) {
                    out.println("La pila esta vacia, no hay cima.");
                } else {
                    out.println("Cima: '" + pila.peek().getValor() + "' (" + pila.peek().getTipo() + ")");
                }
                break;

            case 4:
                out.println(pila.mostrar());
                break;

            case 5:
                pila.vaciar();
                out.println("La pila fue vaciada.");
                break;

            case 6:
                out.print("Nombre de la variable (ej: x, total, a1): ");
                String nombre = leerLinea(in);
                if (!esVariableValida(nombre)) {
                    out.println("Error: el nombre debe iniciar con letra y solo tener letras o numeros.");
                    break;
                }
                out.print("Valor numerico de " + nombre + ": ");
                String textoValor = leerLinea(in);
                if (!esNumero(textoValor)) {
                    out.println("Error: '" + textoValor + "' no es un numero valido.");
                    break;
                }
                guardarVariable(nombre, Double.parseDouble(textoValor));
                out.println("Variable guardada: " + nombre + " = " + textoValor);
                break;

            case 7:
                if (cantidadVariables == 0) {
                    out.println("No hay variables definidas.");
                } else {
                    out.println("Variables definidas:");
                    for (int i = 0; i < cantidadVariables; i++) {
                        out.println("   " + nombresVariables[i] + " = " + formatear(valoresVariables[i]));
                    }
                }
                break;

            case 8:
                out.print("Digite la expresion: ");
                String exp8 = leerLinea(in);
                if (verificarParentesis(exp8)) {
                    out.println("Los parentesis estan BALANCEADOS.");
                } else {
                    out.println("Los parentesis NO estan balanceados.");
                }
                break;

            case 9:
                out.print("Digite la expresion infija (ej: 3 + x * (2 - 1)): ");
                String exp9 = leerLinea(in);
                String tokens9 = validarYTokenizar(exp9, out);
                if (tokens9 != null) {
                    String postfija9 = aPostfija(tokens9.split(" "), out);
                    if (postfija9 != null) {
                        out.println("Expresion postfija: " + postfija9);
                    }
                }
                break;

            case 10:
                out.print("Digite la expresion a evaluar (ej: 3 + x * (2 - 1)): ");
                String exp10 = leerLinea(in);
                String tokens10 = validarYTokenizar(exp10, out);
                if (tokens10 != null) {
                    String postfija10 = aPostfija(tokens10.split(" "), out);
                    if (postfija10 != null) {
                        out.println("Expresion postfija: " + postfija10);
                        double resultado = evaluarPostfija(postfija10, out);
                        if (!Double.isNaN(resultado)) {
                            out.println(">>> RESULTADO: " + exp10.trim() + " = " + formatear(resultado));
                        }
                    }
                }
                break;

            case 0:
                out.println("Gracias por usar el programa. Hasta luego!");
                break;

            default:
                out.println("Opcion invalida. Digite un numero del 0 al 10.");
        }
    }
}


    static void menu(PrintStream out) {
        out.println();
        out.println("---------------- MENU ----------------");
        out.println(" Operaciones de la pila:");
        out.println("   1. Push (insertar token)");
        out.println("   2. Pop (sacar token)");
        out.println("   3. Peek (ver la cima)");
        out.println("   4. Mostrar pila");
        out.println("   5. Vaciar pila");
        out.println(" Variables:");
        out.println("   6. Definir variable");
        out.println("   7. Ver variables");
        out.println(" Analisis de expresiones:");
        out.println("   8. Verificar parentesis balanceados");
        out.println("   9. Convertir infija a postfija");
        out.println("  10. Evaluar expresion");
        out.println("   0. Salir");
        out.print("Seleccione una opcion: ");
    }

    static int leerOpcion(BufferedReader in) throws IOException {
        String linea = in.readLine();
        if (linea == null) {
            return 0;
        }
        try {
            return Integer.parseInt(linea.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static String leerLinea(BufferedReader in) throws IOException {
        String linea = in.readLine();
        if (linea == null) {
            return "";
        }
        return linea.trim();
    }


    static boolean esOperador(String t) {
        return t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/") || t.equals("^");
    }

    static boolean esNumero(String t) {
        if (t.length() == 0) {
            return false;
        }
        char primero = t.charAt(0);
        if (!Character.isDigit(primero) && primero != '.') {
            return false;
        }
        try {
            Double.parseDouble(t);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static boolean esVariableValida(String t) {
        if (t.length() == 0 || !Character.isLetter(t.charAt(0))) {
            return false;
        }
        for (int i = 1; i < t.length(); i++) {
            if (!Character.isLetterOrDigit(t.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static String tipoDeToken(String t) {
        if (esNumero(t)) {
            return "NUMERO";
        } else if (esOperador(t)) {
            return "OPERADOR";
        } else if (t.equals("(") || t.equals(")")) {
            return "PARENTESIS";
        } else if (esVariableValida(t)) {
            return "VARIABLE";
        }
        return "INVALIDO";
    }

    static int prioridad(String op) {
        if (op.equals("^")) {
            return 3;
        } else if (op.equals("*") || op.equals("/")) {
            return 2;
        } else if (op.equals("+") || op.equals("-")) {
            return 1;
        }
        return 0;
    }

    
    static String tokenizar(String expresion) {
        String resultado = "";
        int i = 0;
        while (i < expresion.length()) {
            char c = expresion.charAt(i);

            if (c == ' ') {
                i++;
            } else if (Character.isDigit(c) || c == '.') {
                String numero = "";
                while (i < expresion.length()
                        && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                    numero += expresion.charAt(i);
                    i++;
                }
                resultado += numero + " ";
            } else if (Character.isLetter(c)) {
                String nombre = "";
                while (i < expresion.length() && Character.isLetterOrDigit(expresion.charAt(i))) {
                    nombre += expresion.charAt(i);
                    i++;
                }
                resultado += nombre + " ";
            } else if (esOperador(String.valueOf(c)) || c == '(' || c == ')') {
                resultado += c + " ";
                i++;
            } else {
                return null;
            }
        }
        return resultado.trim();
    }

    static String validarYTokenizar(String expresion, PrintStream out) {
        if (expresion.length() == 0) {
            out.println("Error: la expresion esta vacia.");
            return null;
        }
        if (!verificarParentesis(expresion)) {
            out.println("Error: los parentesis no estan balanceados.");
            return null;
        }
        String tokens = tokenizar(expresion);
        if (tokens == null) {
            out.println("Error: la expresion contiene caracteres no permitidos.");
            return null;
        }
        String[] lista = tokens.split(" ");
        for (int i = 0; i < lista.length; i++) {
            if (tipoDeToken(lista[i]).equals("INVALIDO")) {
                out.println("Error: token invalido '" + lista[i] + "'.");
                return null;
            }
        }
        out.println("Tokens encontrados: " + tokens);
        return tokens;
    }


    /**
     * Verifica parentesis balanceados con una pila:
     * cada '(' se apila y cada ')' desapila un '('.
     * Al final la pila debe quedar vacia.
     */
    static boolean verificarParentesis(String expresion) {
        Pila pila = new Pila();
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (c == '(') {
                pila.push("(", "PARENTESIS");
            } else if (c == ')') {
                if (pila.estaVacia()) {
                    return false;
                }
                pila.pop();
            }
        }
        return pila.estaVacia();
    }

    /**
     * Convierte de notacion infija a postfija (algoritmo Shunting-Yard).
     * Los numeros y variables pasan directo a la salida;
     * los operadores y parentesis esperan en la pila.
     * Muestra cada paso. Devuelve null si hay error.
     */
    static String aPostfija(String[] tokens, PrintStream out) {
        Pila pila = new Pila();
        String salida = "";

        out.println("\n--- Conversion a postfija paso a paso ---");
        for (int i = 0; i < tokens.length; i++) {
            String t = tokens[i];
            String tipo = tipoDeToken(t);

            if (tipo.equals("NUMERO") || tipo.equals("VARIABLE")) {
                salida += t + " ";
            } else if (t.equals("(")) {
                pila.push(t, tipo);
            } else if (t.equals(")")) {
                while (!pila.estaVacia() && !pila.peek().getValor().equals("(")) {
                    salida += pila.pop().getValor() + " ";
                }
                if (pila.estaVacia()) {
                    out.println("Error: parentesis sin pareja.");
                    return null;
                }
                pila.pop();
            } else if (tipo.equals("OPERADOR")) {
                while (!pila.estaVacia() && !pila.peek().getValor().equals("(")
                        && (prioridad(pila.peek().getValor()) > prioridad(t)
                        || (prioridad(pila.peek().getValor()) == prioridad(t) && !t.equals("^")))) {
                    salida += pila.pop().getValor() + " ";
                }
                pila.push(t, tipo);
            }
            out.println("Token: " + String.format("%-6s", t)
                    + " | Pila (cima primero): " + String.format("%-15s", pila.enLinea())
                    + " | Salida: " + salida);
        }

        while (!pila.estaVacia()) {
            Nodo n = pila.pop();
            if (n.getValor().equals("(")) {
                out.println("Error: parentesis sin cerrar.");
                return null;
            }
            salida += n.getValor() + " ";
        }
        return salida.trim();
    }

    /**
     * Evalua una expresion postfija con una pila:
     * los operandos se apilan; cada operador saca dos operandos,
     * calcula y apila el resultado. Devuelve Double.NaN si hay error.
     */
    static double evaluarPostfija(String postfija, PrintStream out) {
        Pila pila = new Pila();
        String[] tokens = postfija.split(" ");

        out.println("\n--- Evaluacion paso a paso ---");
        for (int i = 0; i < tokens.length; i++) {
            String t = tokens[i];
            String tipo = tipoDeToken(t);

            if (tipo.equals("NUMERO")) {
                pila.push(t, "NUMERO");
            } else if (tipo.equals("VARIABLE")) {
                int pos = buscarVariable(t);
                if (pos == -1) {
                    out.println("Error: la variable '" + t + "' no esta definida (use la opcion 6).");
                    return Double.NaN;
                }
                pila.push(formatear(valoresVariables[pos]), "NUMERO");
            } else if (tipo.equals("OPERADOR")) {
                if (pila.getTamanio() < 2) {
                    out.println("Error: faltan operandos para el operador '" + t + "'.");
                    return Double.NaN;
                }
                double b = Double.parseDouble(pila.pop().getValor());
                double a = Double.parseDouble(pila.pop().getValor());
                if (t.equals("/") && b == 0) {
                    out.println("Error: division entre cero.");
                    return Double.NaN;
                }
                double r = operar(a, b, t);
                out.println("   " + formatear(a) + " " + t + " " + formatear(b) + " = " + formatear(r));
                pila.push(formatear(r), "NUMERO");
            }
            out.println("Token: " + String.format("%-6s", t) + " | Pila (cima primero): " + pila.enLinea());
        }

        if (pila.getTamanio() != 1) {
            out.println("Error: la expresion esta mal formada (sobran operandos).");
            return Double.NaN;
        }
        return Double.parseDouble(pila.pop().getValor());
    }

    static double operar(double a, double b, String operador) {
        if (operador.equals("+")) {
            return a + b;
        } else if (operador.equals("-")) {
            return a - b;
        } else if (operador.equals("*")) {
            return a * b;
        } else if (operador.equals("/")) {
            return a / b;
        }
        return Math.pow(a, b);
    }


    static int buscarVariable(String nombre) {
        for (int i = 0; i < cantidadVariables; i++) {
            if (nombresVariables[i].equals(nombre)) {
                return i;
            }
        }
        return -1;
    }

    static void guardarVariable(String nombre, double valor) {
        int pos = buscarVariable(nombre);
        if (pos != -1) {
            valoresVariables[pos] = valor;
        } else if (cantidadVariables < nombresVariables.length) {
            nombresVariables[cantidadVariables] = nombre;
            valoresVariables[cantidadVariables] = valor;
            cantidadVariables++;
        }
    }

    static String formatear(double valor) {
        if (valor == Math.floor(valor) && !Double.isInfinite(valor)) {
            return String.valueOf((long) valor);
        }
        return String.valueOf(valor);
    }
}