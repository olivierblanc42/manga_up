import { UserAddressLittleProjection, PictureDtoRandom } from './type.d';
import { Manga } from './types.d';

//AppUser
export type User = {
    id: number;
    username: string;
    firstname: string;
    lastname: string;
    email: string;
    password: string;
    phone: string;
    createdAt: Date;
    role?: string;

    address: Address;
    gender: Gender;

    picture?: UserPicture;
    comments?: Comment[];
    carts?: Cart[];
};


export type UserTest ={
    id: number;
    username: string;
    firstname: string;
    lastname: string;
    email: string;
    phoneNumber: string;
    userAddressLitle: UserAddressLitle;
};


export type UserResponse = {
    id: number;
    username: string;
    firstname: string;
    lastname: string;
    role?: string;
    phoneNumber?: string;
    email: string;
    createdAt: Date;
    idUserAddress: Address;
    idGendersUser: Gender;
};

export type AppUserProjection = {
    id: number;
    username: string;
    firstname: string;
    lastname: string;
    email: string;
    phoneNumber?: string;
    userAddressLittleProjection: UserAddressLittleProjection;
}

export type AppUserProjections = {
    content: AppUserProjection[];
    size: number;
    totalElements: number;
    totalPages: number;
}



export interface AuthUserInfo {
    username: string;
    role: string;
}
  



export type AppUserRegister ={
    username: string;
    firstname: string;
    lastname: string;
    email: string;
    phoneNumber?: string;
    password: string;
    userAddressLitle: UserRegister;
    genderRegister: GenderRegister;
    role: string | null;
}

//Author
export type Author = {
    id: number;
    lastname: string;
    firstname: string;
    description: string;
    createdAt: Date;
    mangas: Manga[];
}

export type AuthorProjection = {
    id: number;
    lastname: string;
    firstname: string;
    description: string;
    createdAt: Date;
    birthdate: Date;
    url:string;
    genre:string
  
}
export type AuthorProjections = {
    content: AuthorProjection[];
    size: number;
    totalElements: number;
    totalPages: number;
}

export type AuthorDto = {
    lastname: string;
    firstname: string;
    description: string;
    genre:string;
    description:string;
    createdAt: Date;
    birthdate: Date;
}

export type AuthorLigthDto = {
    id: number;
}

export type AuthorDtoRandom = {
    id: number;
    lastname: string;
    firstname: string;
}

export type AuthorLittleProjection = {
    id: number;
    lastname: string;
    firstname: string;
}


export type AuthorWithMangas = {
    id: number;
    lastname: string;
    firstname: string;
    description: string;
    createdAt: Date;
    birthdate: Date;
    url: string;
    genre: string ;
    mangasWithImages: MangasWithImages;
}


//Category
export type Category = {
    id: number;
    label: string;
    description: string;
    createdAt: Date;
    mangas: Manga[];
}

export type CategoryDto = {
    id:number;
    label: string;
    description: string;
    url:String;
    createdAt: Date;
}

export type CategoryLittleDto = {
    id: number;
}

export type CategoryProjection = {
    id: number;
    label: string;
    description: string;
    createdAt: Date;
    url: string;
}

export type CategoriesProjections = {
    content: CategoryProjection[];
    size: number;
    totalElements: number;
    totalPages: number;
}

export type CategoryLittleProjection = {
    id: number;
    label: string;
    description: string;
    createdAt: Date;
}

export type CategoryWithMangas = {
    id: number;
    label: string;
    description: string;
    createdAt: Date;
    url:string
    mangasWithImages: MangasWithImages;
}


//Genre
export type Genre = {
    id: number;
    label: string;
    createdAt: Date;
    mangas: Manga[]
}

export type GenreDto = {
    id: number;
    url: string;
    label: string;
    createdAt: Date;

}

export type genreDtos = {

}
export type GenreLightDto = {
    id: number;
}

export type GenreProjection = {
    id: number;
    label: string;
    url: string;  
    description: string;      
    createdAt: Date;
    
};
export type GenreProjections  = {
    content: GenreProjection[];
    size: number;
    totalElements: number;
    totalPages: number;
}

export type GenreLittleProjection = {
    id: number;
    label: string;
    createdAt: Date;
}

export type GenreWithMangas = {
    id: number;
    label: string;
    url: string;
    createdAt: Date;
    description: string;
    mangasWithImages: MangasWithImages;
}
//GenderUser
export type GenderUser = {
    id: number;
    label: string;
    mangas: AppUser[];
}

export type GenderUserDto = {
    label: string;
}
export type GenderUserProjection = {
    id: number;
    label: string;
    mangas: AppUserLittleProjection[];
}

export type GenderUserProjections = {
    content: GenderUserProjection[];
    size: number;
    totalElements: number;
    totalPages: number;
}
export type GenderRegister = {
    id: number;
    label: string;
}

//Manga

export type FavoriteManga = {
    favorite: boolean;
}

export type MangaProjection = {
    id: number;
    title: string;
    subtitle: string;
    summary: string;
    releaseDate: Date;
    price: number;
    priceHt: number;
    inStock: boolean;
    active: boolean;
    idCategories: CategoryLittleProjection;
    pictures: PictureSoloManga[];
    genres: GenreLittleProjection[];
    authors: AuthorLittleProjection[];
}

export type MangaProjections = {
    content: MangaProjection[];
    size: number;
    totalElements: number;
    totalPages: number;
}

export type MangaDtoRandom = {
    id_mangas: number;
    title: string;
    authors: AuthorDtoRandom[];
    picture: string;
}
export interface MangaOne {
    id: number;
    title: string;
    subtitle: string | null;
    summary: string;
    price: number;
    idCategories: {
        label: string;
        description: string;
    };
    genres: GenreDto[];
    authors: AuthorDtoRandom[] | undefined;
    picture: string;
}


export type MangaLittleProjection = {
    id: number;
    title: string;
    pictures: Picture[];
}

export type MangaWithImages = {
    id: number;
    pictureId: string;
    pictureUrl:string;
    title: string;
}

export type MangasWithImages = {
    content: MangaWithImages[];
    size: number;
    totalElements: number;
    totalPages: number;
}

export type MangaPagination = {
    id: number;
    pictureId: string;
    pictureUrl: string;
    title: string;
}
export type MangaPaginations = {
    content: MangaPagination[];
    size: number;
    totalElements: number;
    totalPages: number;
}


export type MangaBaseProjection = {
    id: number;
    title: string;
    idImage: number;
    pictureUrl: string;
    authorFullName: string;
}
export type MangaBaseProjections = {
    content: MangaBaseProjection[];
    size: number;
    totalElements: number;
    totalPages: number;
}

//UserAddress
export type UserAddress = {
    id: number;
    line1: string;
    line2: string;
    line3: string;
    city: string;
    createdAt: Date;
    postalCode: string;
}

export type UserAddressDto = {
    id: number;
    line1: string;
    line2: string;
    line3: string;
    city: string;
    createdAt: Date;
    postalCode: string;
}

export type UserAddressProjection = {
    id: number;
    line1: string;
    line2: string;
    line3: string;
    city: string;
    createdAt: Date;
    postalCode: string;
    appUser: AppUserLittleProjection[];
}
export type UserAddressesProjection = {
    content: UserAddressProjection[];
    size: number;
    totalElements: number;
    totalPages: number;
}

export type UserAddressLittleProjection = {
    id: number;
    line1: string;
    line2: string;
    line3: string;
    city: string;
    createdAt: Date;
    postalCode: string;
}

export type UserAddressLitle = {
    line1: string;
    line2: string;
    line3: string;
    city: string;
    createdAt: Date;
    postalCode: string;
}

export type UserRegister = {
    line1: string;
    line2: string;
    line3: string;
    city: string;
    createdAt: Date;
    postalCode: string;
}

export interface PictureDtoRandom {
    id: number;
    url: string;
}

export type Picture = {
    id: number;
    url: string;
}

export type PictureSoloManga = {
    id: number;
    url: string;
    isMain: Boolean;
}

