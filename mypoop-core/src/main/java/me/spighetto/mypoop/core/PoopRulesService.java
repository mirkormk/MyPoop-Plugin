package me.spighetto.mypoop.core;

/**
 * Servizio di dominio puro con regole per il "poop".
 * Nessuna dipendenza da Bukkit/Paper.
 */
public final class PoopRulesService {

    /**
     * Ritorna true se l'accumulo ha raggiunto o superato la soglia di trigger.
     */
    public boolean shouldTrigger(int accumulated, int triggerThreshold) {
        return accumulated >= triggerThreshold;
    }

    /**
     * Ritorna true se l'accumulo ha raggiunto o superato il limite massimo.
     */
    public boolean isAtLimit(int accumulated, int limit) {
        return accumulated >= limit;
    }

    /**
     * Aggiunge il delta all'accumulo corrente senza superare il limite massimo.
     * Restituisce il nuovo valore (saturato al limite).
     */
    public int accumulateWithinLimit(int current, int delta, int limit) {
        long sum = (long) current + (long) delta; // evita overflow int
        if (sum >= limit) return limit;
        if (sum <= Integer.MIN_VALUE) return Integer.MIN_VALUE; // protezione estrema
        return (int) sum;
    }
}

