package ru.textanalysis.synonims.data;

public class Word {
    
    private String word;
    private int frequency;  //Части речи: 9 - нар, 12 - предлог, 17 - сущ, 18 - прил, 20 - гл, 19 21 22 - дееприч, 31 - мест

    public Word(String word, int frequency_abs, double frequency, String part) {
        this.word = word;
        this.frequency = frequency_abs;
    }

    public Word() {
        this.word = "";
        this.frequency = 0;
    }

    public Word(Word other) {
        this.word = other.word;
        this.frequency = other.frequency;
    }

    public Word(String word) {
        this.word = word;
        this.frequency = 0;
    }

    public Word(String word, int frAbs) {
        this.word = word;
        this.frequency = frAbs;
    }

    public String getWord() {
        return this.word;
    }

    public int getFrequencyAbs() {
        return this.frequency;
    }

    public void setFrequencyAbs(int frequency) {
        this.frequency = frequency;
    }

    protected void increaseFrequencyAbs(int frequency) {
        this.frequency = this.frequency + frequency;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean equals(Word o) {
        if (o.getWord().equals(this.word)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equals(String word) {
        if (word.equals(this.word)) {
            return true;
        } else {
            return false;
        }
    }
    
    public void clear() {
        this.word = null;
        this.frequency = 0;
    }

    public int compare(Word o) {
        if (this.frequency > o.frequency) {
            return 1;
        } else if (this.frequency < o.frequency) {
            return -1;
        } else {
            return 0;
        }
    }

    private void printCharacteristic() {
        System.out.println(this.word + ", " + this.getFrequencyAbs());
    }
    
    @Override
    public String toString() {
        return (this.word + ", " + this.getFrequencyAbs());
    }
/*
    public static void main(String[] args) {
        Word word = new Word ("глава", 4);
        word.printCharacteristic();
    }
*/
}
