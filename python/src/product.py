from dataclasses import dataclass


@dataclass(frozen=True)
class Product:
    id: str
    name: str
    price: int  # в копейках, чтобы float не использовать для рублей
    stock: int