package com.kobi.progressservice.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@AllArgsConstructor
public class PageResponse<T> {
    List<T> content; //Danh sach doi tuong muon hien thi
    int pageNo; //So trang hien tai
    int pageSize; //Kich thuoc trang
    long totalElement; //Tong so doi tuong hien thi tren 1 trang
    int totalPages; // Tonn so trang
    boolean last; //Co phai trang cuoi khong
}
