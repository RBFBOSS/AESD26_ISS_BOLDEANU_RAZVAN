CREATE INDEX IF NOT EXISTS idx_deliveries_customer ON deliveries(customer);
CREATE INDEX IF NOT EXISTS idx_deliveries_timestamp ON deliveries("timestamp");
CREATE INDEX IF NOT EXISTS idx_deliveries_quantity ON deliveries(quantity);
CREATE INDEX IF NOT EXISTS idx_customers_address_lower ON customers (lower(address));
