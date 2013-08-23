package org.squadra.atenea.history;

import java.util.Comparator;

/**
 * Comparador de items de historial.
 * Se utiliza para poder ordenar los items por fecha.
 * @author Leandro Morrone
 *
 */
public class HistoryItemComparator implements Comparator<HistoryItem> {
    @Override
    public int compare(HistoryItem o1, HistoryItem o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}