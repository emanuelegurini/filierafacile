-- Fix categoria_prodotto enum values to match Java enum CategoriaProdotto
-- Available values: ORTOFRUTTA, CASEARI, CARNI_E_SALUMI, CONSERVE_E_MARMELLATE, ALTRO

UPDATE prodotto
SET categoria_prodotto = 'ORTOFRUTTA'
WHERE categoria_prodotto = 'ORTAGGI';

UPDATE prodotto
SET categoria_prodotto = 'ALTRO'
WHERE categoria_prodotto = 'CONDIMENTI';

UPDATE prodotto
SET categoria_prodotto = 'ALTRO'
WHERE categoria_prodotto = 'CEREALI';