package com.cloudera.knittingboar.sgd;

import java.util.ArrayList;

import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.Matrix;

import com.cloudera.knittingboar.utils.Utils;

/**
 * Gradient accumulation buffer for parallel sgd
 * 
 * basically a wrapper around another Matrix object to hold the intermediate updates per batch
 * which are sent to the master node for accumulation into the global parameter vector
 * 
 * @author jpatterson
 *
 */
public class GradientBuffer {

  
  protected Matrix gamma; // this is the saved updated gradient we merge at the super step

  private int AccumulatedGradientsCount = 0;
  private int numCategories = 2; // default
  
  public GradientBuffer(int numCategories, int numFeatures) {
    
    this.numCategories = numCategories;
    this.gamma = new DenseMatrix(numCategories - 1, numFeatures);
    
  }
  
  public void setMatrix( Matrix m ) {
    
    this.gamma = m;
    
  }

  public Matrix getMatrix() {
    //close();
    return this.gamma;
  }

  public void setCell(int i, int j, double gammaIJ) {
    this.gamma.set(i, j, gammaIJ);
  }

  public double getCell(int row, int col) {
    return this.gamma.get(row, col);
  }

  public int numFeatures() {
    return this.gamma.numCols();
  }
  
  public int numCategories() {
    return this.numCategories;
  }

  private void ClearGradientBuffer() {

    this.gamma = this.gamma.like();
    
  }
  
  public void SetupMerge() {
    
    this.AccumulatedGradientsCount = 0;
    this.ClearGradientBuffer();
    
  }
  
  public void Accumulate( GradientBuffer other_gamma ) {
    
    for ( int row = 0; row < this.gamma.rowSize(); row++ ) {
      
      for ( int col = 0; col < this.gamma.columnSize(); col++ ) {
    
        double old_this_val = this.gamma.get(row, col);
        double other_val = other_gamma.getCell(row, col);
        this.gamma.set(row, col, old_this_val + other_val );
        
      }
      
    }
       
    this.AccumulatedGradientsCount++;
    
  }
  
  /**
   * Copies another GradientBufffer
   * 
   * @param other
   */
  public void Copy( GradientBuffer other ) {
    
    this.Accumulate( other );
    
  }
  
  public static GradientBuffer Merge( ArrayList<GradientBuffer> gammas ) {
    
    int numFeatures = gammas.get(0).numFeatures();
    int numCategories = gammas.get(0).numCategories();
    
    GradientBuffer merged = new GradientBuffer(numCategories, numFeatures);
    
    // accumulate all the gradients into buffers
    
    for ( int x = 0; x < gammas.size(); x++ ) {
      
      merged.Accumulate(gammas.get(x));
      
    }
    
    // calc average
    
    
    return merged;
    
  }
  
  /**
   * Clears all values in matrix back to 0s
   */
  public void Reset() {
    
   for ( int row = 0; row < this.gamma.rowSize(); row++ ) {
      
      for ( int col = 0; col < this.gamma.columnSize(); col++ ) {
    
        this.gamma.set(row, col, 0 );
        
      }
      
    }    
    
  }
  
  public void Debug() {
    
    System.out.println( "\nGamma: " );
    
    for ( int x = 0; x < this.gamma.rowSize(); x++ ) {
      
      Utils.PrintVectorSectionNonZero( this.gamma.viewRow(x), 3 );
      
    }
    
  }

  
  
  public void DebugRowInGamma( int row ) {
    
    System.out.println( "\nGamma: " );
      Utils.PrintVectorSectionNonZero( this.gamma.viewRow(row), 3 );
      
    
  }  
  
  
}
