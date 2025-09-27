-- Fix unita_misura enum values to match Java enum UnitaMisura
-- Available values: KG, LT, PEZZO, ETTO, MAZZO

UPDATE prodotto
SET unita_misura = 'LT'
WHERE unita_misura = 'LITRI';