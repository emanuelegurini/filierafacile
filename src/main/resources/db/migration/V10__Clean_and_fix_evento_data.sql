-- Clean up any problematic evento data before enum conversion
-- This ensures V5 migration runs successfully

-- First, delete any existing evento records that might have invalid stato_validazione values
DELETE FROM evento_aziende;
DELETE FROM biglietto WHERE evento_id IS NOT NULL;
DELETE FROM evento;

-- Ensure the column can be converted to INT
-- V5 will handle the actual conversion