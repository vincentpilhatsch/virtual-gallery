export interface Artwork {
  id: number, // id notwendig?
  title: string,
  width: number,
  height: number,
  yearOfCreation: string,
  uploadedAt: Date,
  technic: string,
  price: number,
  sold: boolean,
  image: string
}
